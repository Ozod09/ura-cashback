package itca.uz.ura_cashback_2.service;


import itca.uz.ura_cashback_2.entity.*;
import itca.uz.ura_cashback_2.entity.enums.RoleName;
import itca.uz.ura_cashback_2.exception.ResourceNotFoundException;
import itca.uz.ura_cashback_2.mappers.CompanyMapper;
import itca.uz.ura_cashback_2.mappers.UserMapper;
import itca.uz.ura_cashback_2.payload.*;
import itca.uz.ura_cashback_2.repository.*;
import itca.uz.ura_cashback_2.utils.CommonUtils;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.web.client.ResourceAccessException;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


@Service

public class AuthService{

    private final CompanyMapper companyMapper;
    private final AuthRepository authRepository;
    private final CompanyRepository companyRepository;
    private final RoleRepository roleRepository;
    private final CompanyUserRoleRepository companyUserRoleRepository;
    private final OrderRepository orderRepository;
    private final CompanyUserRoleService companyUserRoleService;
    private final UserMapper mapper;


    public AuthService(CompanyMapper companyMapper, AuthRepository authRepository, UserMapper mapper,
                       CompanyRepository companyRepository, RoleRepository roleRepository, CompanyUserRoleRepository companyUserRoleRepository, OrderRepository orderRepository, CompanyUserRoleService companyUserRoleService) {
        this.companyMapper = companyMapper;
        this.authRepository = authRepository;
        this.companyRepository = companyRepository;
        this.roleRepository = roleRepository;
        this.companyUserRoleRepository = companyUserRoleRepository;
        this.orderRepository = orderRepository;
        this.companyUserRoleService = companyUserRoleService;
        this.mapper = mapper;
    }

    public ApiResponse<?> addRegisterClient(AuthDto authDto) {
        AuthDto save = addUser(authDto);
        companyUserRoleService.addCompanyUserRole(new CompanyUserRole(), save.getId(), authDto.getCompanyId(), 4);
        return new ApiResponse<>("User saved", 200);
    }



    public Long addCompanyAdmin(AuthDto authDto){
        return addUser(authDto).getId();
    }

    public AuthDto addKassa(AuthDto authDto) {
        AuthDto save = addUser(authDto);
        companyUserRoleService.addCompanyUserRole(new CompanyUserRole(), save.getId(), authDto.getCompanyId(), 3);
        return save;
    }

    public Long editCompanyAdmin(AuthDto authDto, User user){
        return editUser(authDto, user).getId();
    }

    public AuthDto editKassa(AuthDto authDto, User user) {
        AuthDto save = editUser(authDto, user);
        CompanyUserRole companyUserRole = companyUserRoleRepository.getKassir(authDto.getId(), 3).orElseThrow(() -> new ResourceNotFoundException(404, "companyUserRole", "id", save));
        companyUserRoleService.addCompanyUserRole(companyUserRole, companyUserRole.getUserId(), companyUserRole.getCompanyId(), companyUserRole.getRoleId());
        return save;
    }

    public ApiResponse<?> deleteClient(Long id) {
        CompanyUserRole companyUserRole = companyUserRoleRepository.getKassir(id, 3).orElseThrow(() -> new ResourceNotFoundException(404, "companyUserRole", "id", id));
        authRepository.deleteById(id);
        companyUserRoleRepository.deleteById(companyUserRole.getId());
        return new ApiResponse<>("Successfully delete kassir", 200);
    }


    public ApiResponse<?> activeUser(Long id){
        User user = authRepository.findById(id).orElseThrow(() -> new ResourceAccessException("getUser"));
        if (user.getActive()==1){
            user.setActive((byte) 0);
        }else{
            user.setActive((byte) 1);
        }
        authRepository.save(user);
        return new ApiResponse<>("Successfully active", 200);
    }

    public void editUserSalary(User user, int salary) {
        user.setSalary(salary);
        authRepository.save(user);
    }


    public ResPageable getUserList(int page, int size) throws Exception {
        Page<User> allUser = authRepository.findAll(CommonUtils.getPageable(page, size));
        return new ResPageable(
                page,
                size,
                allUser.getTotalElements(),
                allUser.getTotalPages(),
                new ArrayList<>(allUser.getContent())
        );
    }

    public User getOneUser(Long id) {
        return authRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException(404, "dd", "aa", id));
    }

    public User findByPhoneNumber(String phoneNumber) {
        return authRepository.findByPhoneNumber(phoneNumber).orElseThrow(() -> new ResourceNotFoundException(404, "User", "phoneNumber", phoneNumber));
    }


    public ApiResponse<?> editPassword(ReqPassword reqPassword){
        User user = authRepository.findById(reqPassword.getUserId()).orElseThrow(() -> new ResourceNotFoundException(404, "User", "id", reqPassword));
        if (user.getPassword().equals(reqPassword.getJoriyPassword())){
            if (reqPassword.getPassword().equals(reqPassword.getPrePassword())){
                user.setPassword(reqPassword.getPassword());
                authRepository.save(user);
                return new ApiResponse<>("SuccessFully", 200);
            }
            return new ApiResponse<>("password and prePassword equals", 401);
        }
        return new ApiResponse<>("Password not found", 401);
    }

    public CompanyDto loginCompany(ReqLogin reqLogin){
        CompanyDto companyDto = new CompanyDto();
        User user = authRepository.findPhoneAndPassword(reqLogin.getPhoneNumber(), reqLogin.getPassword()).orElseThrow(() -> new ResourceNotFoundException(404, "User", "phoneNumber and password", reqLogin));
        CompanyUserRole companyUserRole = companyUserRoleRepository.findId(user.getId()).orElseThrow(() -> new ResourceNotFoundException(404, "companyUserRole", "id", reqLogin));
        Role role = roleRepository.findId(companyUserRole.getRoleId()).orElseThrow(() -> new ResourceNotFoundException(403, "Role ", "Admin", companyUserRole.getRoleId()));
        Company company = companyRepository.findById(companyUserRole.getCompanyId()).orElseThrow(()-> new ResourceAccessException("GetCompany"));
        if(company.getActive()==1) {
            if (role.getRoleName().equals(RoleName.ROLE_ADMIN) || role.getRoleName().equals(RoleName.ROLE_KASSA)) {
                companyMapper.fromCompany(company);
                companyDto.setActive1(company.getActive()==1);
                companyDto.setId(company.getId());
                companyDto.setUser(user);
                List<User> kassaList = new ArrayList<>();
                List<User> clintList = new ArrayList<>();
                List<OrderDto> kassaOrderList = getKassaOrAdminOrder(user);
                List<OrderDto> orderList = new ArrayList<>(kassaOrderList);
                kassaList.add(user);
                Role role1 = roleRepository.findRoleName(RoleName.ROLE_KASSA).orElseThrow(() -> new ResourceNotFoundException(403, "Role", "Kassir", role));
                for (CompanyUserRole companyUserRole1 : companyUserRoleRepository.companyIdAndRoleId(company.getId(), role1.getId())) {
                    User user1 = authRepository.findById(companyUserRole1.getUserId()).orElseThrow(() -> new ResourceNotFoundException(404, "User", "id", companyUserRole1));
                    List<OrderDto> kassaOrAdminOrder = getKassaOrAdminOrder(user1);
                    orderList.addAll(kassaOrAdminOrder);
                    kassaList.add(user1);
                }

                Role role2 = roleRepository.findRoleName(RoleName.ROLE_USER).orElseThrow(() -> new ResourceNotFoundException(403, "Role", "User", role));
                for (CompanyUserRole companyUserRole2 : companyUserRoleRepository.companyIdAndRoleId(company.getId(), role2.getId())) {
                    User user1 = authRepository.findById(companyUserRole2.getUserId()).orElseThrow(() -> new ResourceNotFoundException(404, "User", "id", companyUserRole2));
                    clintList.add(user1);
                }
                companyDto.setKassa(kassaList);
                companyDto.setClint(clintList);
                companyDto.setOrders(orderList);
                companyDto.setResStatistic(getCompanyStatistic(company.getId()));
                return companyDto;
            }
        }
        return null;
    }

    public ResStatistic getCompanyStatistic(Long companyId){
        Set<Long> countClient = new HashSet<>();
        int allBalance = 0;
        int clientNaqtTulovComp = 0;
        int clientCompCash = 0;
        int companyClientCash = 0;
        int clientCash = 0;
        for (Order order : orderRepository.findCreatedBy(companyId)) {
            countClient.add(order.getClient().getId());
            allBalance += order.getCash_price();
            clientCompCash += order.getClientCompCash();
            companyClientCash += order.getCompanyClientCash();
            clientCash += order.getClient().getSalary();
        }
        ResStatistic resStatistic = new ResStatistic();
        resStatistic.setJamiClient(countClient.size());
        resStatistic.setAllBalance(allBalance);
        resStatistic.setClientCompCash(clientCompCash);
        resStatistic.setCompanyClientCash(companyClientCash);
        resStatistic.setClientCash(clientCash);
        return resStatistic;
    }

    public List<OrderDto> getKassaOrAdminOrder(User user){
        List<OrderDto> orderDtoList = new ArrayList<>();
        for (Order kassaOrder : orderRepository.findCreatedBy(user.getId())) {
            OrderDto orderDto = new OrderDto();
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy/MM/dd");
            orderDto.setDate(simpleDateFormat.format(kassaOrder.getCreatedAt()));
            orderDto.setId(kassaOrder.getId());
            orderDto.setCreatedBy(kassaOrder.getCreatedBy());
            orderDto.setAdmin(authRepository.findById(kassaOrder.getCreatedBy()).orElseThrow(() -> new ResourceAccessException("GetKassir")));
            orderDto.setClient(kassaOrder.getClient());
            orderDto.setCashback(kassaOrder.getCompanyClientCash());
            orderDto.setCash_price(kassaOrder.getCash_price());
            orderDtoList.add(orderDto);
        }
        return orderDtoList;
    }

    public AuthDto editUser(AuthDto authDto, User user){
        if (authDto.getPassword().equals(authDto.getPrePassword())) {
            User user1 = mapper.fromDto(authDto);
            user1.setId(user.getId());
            User save = authRepository.save(user1);
            AuthDto authDto1 = mapper.fromUser(save);
            authDto1.setCompanyId(authDto.getCompanyId());
            return authDto1;
        }
        return null;
    }

    public AuthDto addUser(AuthDto authDto) {
        try {
            if (!authRepository.equalsUser(authDto.getPhoneNumber(), authDto.getEmail()).isPresent()){
                if (authDto.getPassword().equals(authDto.getPrePassword())) {
                    User user = mapper.fromDto(authDto);
                    User save = authRepository.save(user);
                    AuthDto authDto1 = mapper.fromUser(save);
                    authDto1.setCompanyId(authDto.getCompanyId());
                    return authDto1;
                }
            }

        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }



//    public ApiResponse<?> loginSuperAdmin(ReqLogin reqLogin){
//        User superAdmin = authRepository.findPhoneAndPassword(reqLogin.getPhoneNumber(), reqLogin.getPassword()).orElseThrow(() -> new ResourceNotFoundException(404, "User", "id", reqLogin));
//        CompanyUserRole companyUserRole = companyUserRoleRepository.findByUserAndRole(superAdmin.getId(), roleRepository.findRoleName(RoleName.ROLE_SUPER_ADMIN).orElseThrow(() -> new ResourceNotFoundException(403, "Role", "roleName", superAdmin)).getId());
//        Role role = roleRepository.findId(companyUserRole.getRoleId()).orElseThrow(() -> new ResourceNotFoundException(403, "Role", "id", companyUserRole.getRoleId()));
//        if (role.getRoleName().equals(RoleName.ROLE_SUPER_ADMIN)){
//            return new ApiResponse<>("success", 200);
//        }
//        return new ApiResponse<>("Super admin not found", 401);
//    }




}
