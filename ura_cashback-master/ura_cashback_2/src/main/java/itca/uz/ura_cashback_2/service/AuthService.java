package itca.uz.ura_cashback_2.service;


import itca.uz.ura_cashback_2.entity.*;
import itca.uz.ura_cashback_2.entity.enums.RoleName;
import itca.uz.ura_cashback_2.exception.ResourceNotFoundException;
import itca.uz.ura_cashback_2.mappers.CompanyMapper;
import itca.uz.ura_cashback_2.mappers.UserMapper;
import itca.uz.ura_cashback_2.payload.*;
import itca.uz.ura_cashback_2.repository.*;
import itca.uz.ura_cashback_2.utils.CommonUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.web.client.ResourceAccessException;

import java.text.SimpleDateFormat;
import java.util.*;


@Service
@RequiredArgsConstructor
public class AuthService{

    private final CompanyMapper companyMapper;
    private final AuthRepository authRepository;
    private final CompanyRepository companyRepository;
    private final RoleRepository roleRepository;
    private final CompanyUserRoleRepository companyUserRoleRepository;
    private final OrderRepository orderRepository;
    private final CompanyUserRoleService companyUserRoleService;
    private final UserMapper mapper;



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
        Page<User> allUser = authRepository.findAll(CommonUtils.getPageable(page * size, size));
        return new ResPageable(
                page * size,
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

    public List<OrderDto> companyOrder(Long id){
        List<OrderDto> orderDtoList = new ArrayList<>();
        List<CompanyUserRole> companyUserRoleAdmin = companyUserRoleRepository.companyIdAndRoleId(id, 2);
        for (CompanyUserRole companyUserRole : companyUserRoleAdmin) {
            List<OrderDto> kassaOrAdminOrder = getKassaOrAdminOrder(authRepository.findById(companyUserRole.getUserId()).orElseThrow(() -> new ResourceNotFoundException(404, "User", "id", companyUserRole.getUserId())));
            orderDtoList.addAll(kassaOrAdminOrder);
        }
        List<CompanyUserRole> companyUserRolesKasser = companyUserRoleRepository.companyIdAndRoleId(id, 3);
        for (CompanyUserRole companyUserRole : companyUserRolesKasser) {
            List<OrderDto> kassaOrAdminOrder = getKassaOrAdminOrder(authRepository.findById(companyUserRole.getUserId()).orElseThrow(() -> new ResourceNotFoundException(404, "User", "id", companyUserRole.getUserId())));
            orderDtoList.addAll(kassaOrAdminOrder);
        }
        return orderDtoList;
    }

    public List<User> companyKassaOrClient(Long id, Integer roleId){
        List<User> users = new ArrayList<>();
        List<CompanyUserRole> companyUserRoles = companyUserRoleRepository.companyIdAndRoleId(id, roleId);
        for (CompanyUserRole companyUserRole : companyUserRoles) {
            User user = authRepository.findById(companyUserRole.getUserId()).orElseThrow(() -> new ResourceNotFoundException(404, "User", "id", companyUserRole.getUserId()));
            users.add(user);
        }
        return users;
    }

    public CompanyDto loginCompany(ReqLogin reqLogin){
        User user = authRepository.findPhoneAndPassword(reqLogin.getPhoneNumber(), reqLogin.getPassword()).orElseThrow(() -> new ResourceNotFoundException(404, "User", "phoneNumber and password", reqLogin));
        CompanyUserRole companyUserRole = companyUserRoleRepository.findId(user.getId()).orElseThrow(() -> new ResourceNotFoundException(404, "companyUserRole", "id", reqLogin));
        Role role = roleRepository.findId(companyUserRole.getRoleId()).orElseThrow(() -> new ResourceNotFoundException(403, "Role ", "Admin", companyUserRole.getRoleId()));
        Company company = companyRepository.findById(companyUserRole.getCompanyId()).orElseThrow(()-> new ResourceAccessException("GetCompany"));
        if(company.getActive()==1) {
            if (role.getRoleName().equals(RoleName.ROLE_ADMIN)) {
                CompanyDto companyDto1 = companyMapper.fromCompany(company);
                companyDto1.setActive1(company.getActive()==1);
                companyDto1.setId(company.getId());
                companyDto1.setUser(user);
                companyDto1.setResStatistic(getCompanyStatistic(company.getId()));
                return companyDto1;
            }
        }
        return null;
    }

    public ResStatistic getCompanyStatistic(Long companyId){
        Set<Long> allClient = new HashSet<>();
        int allBalance = 0;
        int companyClientCash = 0;
        int urtachaCheck = 0;
        int clientCash = 0;
        for (Order order : orderRepository.findCreatedBy(companyId)) {
            allBalance += order.getCash_price();
            companyClientCash += order.getCompanyClientCash();
            allClient.add(order.getClient().getId());
        }
        if(allBalance != 0) {
            urtachaCheck = allBalance / allClient.size();
        }
        for(Long id : allClient){
            clientCash += authRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException(404,"User","id",id)).getSalary();
        }
        ResStatistic resStatistic = new ResStatistic();
        resStatistic.setJamiClient(allClient.size());
        resStatistic.setAllBalance(allBalance);
        resStatistic.setCompanyClientCash(companyClientCash);
        resStatistic.setClientCash(clientCash);
        resStatistic.setUrtachaCheck(urtachaCheck);
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

}
