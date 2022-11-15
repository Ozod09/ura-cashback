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

import java.util.ArrayList;
import java.util.List;


@Service

public class AuthService{

    private final CompanyMapper companyMapper;
    private final AuthRepository authRepository;
    private final AttachmentRepository attachmentRepository;
    private final CompanyRepository companyRepository;
    private final RoleRepository roleRepository;
    private final CompanyUserRoleRepository companyUserRoleRepository;
    private final OrderRepository orderRepository;
    private final CompanyUserRoleService companyUserRoleService;
    private final UserMapper mapper;


    public AuthService(CompanyMapper companyMapper, AuthRepository authRepository, UserMapper mapper, AttachmentRepository attachmentRepository,
                       CompanyRepository companyRepository, RoleRepository roleRepository, CompanyUserRoleRepository companyUserRoleRepository, OrderRepository orderRepository, CompanyUserRoleService companyUserRoleService) {
        this.companyMapper = companyMapper;
        this.authRepository = authRepository;
        this.attachmentRepository = attachmentRepository;
        this.companyRepository = companyRepository;
        this.roleRepository = roleRepository;
        this.companyUserRoleRepository = companyUserRoleRepository;
        this.orderRepository = orderRepository;
        this.companyUserRoleService = companyUserRoleService;
        this.mapper = mapper;
    }

    public ApiResponse<?> addRegisterClient(AuthDto authDto) {
        AuthDto save = addUser(authDto);
        companyUserRoleService.addCompanyUserRole(new CompanyUserRole(), save.getId(), authDto.getCompanyId(), roleRepository.findRoleName(RoleName.ROLE_USER).orElseThrow(() -> new ResourceNotFoundException( 403, "Role", "roleName", save.getId())).getId());
        return new ApiResponse<>("User saved", 200);
    }

    public Long addCompanyAdmin(AuthDto authDto){
        return addUser(authDto).getId();
    }

    public Long editCompanyAdmin(AuthDto authDto, User user){
        return addUser(authDto).getId();
    }

    public AuthDto addKassa(AuthDto authDto) {
        AuthDto save = addUser(authDto);
        companyUserRoleService.addCompanyUserRole(new CompanyUserRole(), save.getId(), authDto.getCompanyId(), roleRepository.findRoleName(RoleName.ROLE_KASSA).orElseThrow(() -> new ResourceNotFoundException(403, "Role", "Kassir", save)).getId());
        return save;
    }

    public AuthDto editKassa(AuthDto authDto, User user) {
        AuthDto save = addUser(authDto);
        CompanyUserRole companyUserRole = companyUserRoleRepository.findByUserAndRole(authDto.getId(), roleRepository.findRoleName(RoleName.ROLE_KASSA).orElseThrow(() -> new ResourceNotFoundException(403, "Role", "Kassir", save)).getId());
        companyUserRoleService.addCompanyUserRole(companyUserRole, companyUserRole.getUserId(), companyUserRole.getCompanyId(), companyUserRole.getRoleId());
        return save;
    }

    public ApiResponse<?> deleteClient(Long id) {
        CompanyUserRole companyUserRole = companyUserRoleRepository.findByUserAndRole(id, roleRepository.findRoleName(RoleName.ROLE_KASSA).orElseThrow(() -> new ResourceNotFoundException(403, "Role", "Kassir", id)).getId());
        companyUserRoleRepository.deleteById(companyUserRole.getId());
        authRepository.deleteById(id);
        return new ApiResponse<>("Successfully delete client", 200);
    }


    public ApiResponse<?> activeUser(Long id){
        User user = authRepository.findById(id).orElseThrow(() -> new ResourceAccessException("getUser"));
        if (user.getActive()==1){
            user= User.builder()
                    .active((byte) 0).build();
        }else{
            user= User.builder()
                    .active((byte) 1).build();
        }
        authRepository.save(user);
        return new ApiResponse<>("Successfully active", 200);
    }

    public void editUserSalary(int salary) {
        User user = User.builder()
                .salary(salary).build();
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
        return authRepository.findById(id).orElseThrow(() -> new ResourceAccessException("getUser"));
    }

    public User findByPhoneNumber(String phoneNumber) {
        return authRepository.findUserPhone(phoneNumber);
    }


    public ApiResponse<?> editPassword(ReqPassword reqPassword){
        User user = authRepository.findByIdEquals(reqPassword.getUserId());
        if (user.getPassword().equals(reqPassword.getJoriyPassword())){
            if (reqPassword.getPassword().equals(reqPassword.getPrePassword())){
                user= User.builder()
                        .password(reqPassword.getPassword()).build();
                authRepository.save(user);
                return new ApiResponse<>("SuccessFully", 200);
            }
            return new ApiResponse<>("password and prePassword equals", 401);
        }
        return new ApiResponse<>("Password not found", 401);
    }

    public CompanyDto loginCompany(ReqLogin reqLogin){
        CompanyDto companyDto = new CompanyDto();
        User user = authRepository.findPhoneAndPassword(reqLogin.getPhoneNumber(), reqLogin.getPassword());
        CompanyUserRole companyUserRole = companyUserRoleRepository.findId(user.getId());
        Role role = roleRepository.findId(companyUserRole.getRoleId()).orElseThrow(() -> new ResourceNotFoundException(403, "Role ", "Admin", companyUserRole.getRoleId()));
        Company company = companyRepository.findById(companyUserRole.getCompanyId()).orElseThrow(()-> new ResourceAccessException("GetCompany"));
        if(company.getActive()==1) {
            if (role.getRoleName().equals(RoleName.ROLE_ADMIN) || role.getRoleName().equals(RoleName.ROLE_KASSA)) {
                companyMapper.fromCompany(company);
                companyDto.setActive1(company.getActive()==1);
                companyDto.setUser(user);
                List<User> kassaList = new ArrayList<>();
                List<User> clintList = new ArrayList<>();
                List<OrderDto> kassaOrderList = getKassaOrAdminOrder(user);
                List<OrderDto> orderList = new ArrayList<>(kassaOrderList);
                kassaList.add(user);
                Role role1 = roleRepository.findRoleName(RoleName.ROLE_KASSA).orElseThrow(() -> new ResourceNotFoundException(403, "Role", "Kassir", role));
                for (CompanyUserRole companyUserRole1 : companyUserRoleRepository.findByCompanyIdEqualsAndRoleIdEquals(company.getId(), role1.getId())) {
                    User user1 = authRepository.findById(companyUserRole1.getUserId()).orElseThrow(() -> new ResourceNotFoundException(404, "User", "id", companyUserRole1));
                    List<OrderDto> kassaOrAdminOrder = getKassaOrAdminOrder(user1);
                    orderList.addAll(kassaOrAdminOrder);
                    kassaList.add(user1);
                }

                Role role2 = roleRepository.findRoleName(RoleName.ROLE_USER).orElseThrow(() -> new ResourceNotFoundException(403, "Role", "User", role));
                for (CompanyUserRole companyUserRole2 : companyUserRoleRepository.findByCompanyIdEqualsAndRoleIdEquals(company.getId(), role2.getId())) {
                    User user1 = authRepository.findById(companyUserRole2.getUserId()).orElseThrow(() -> new ResourceNotFoundException(404, "User", "id", companyUserRole2));
                    clintList.add(user1);
                }
                companyDto.setKassa(kassaList);
                companyDto.setClint(clintList);
                companyDto.setOrders(orderList);
                return companyDto;
            }
        }
        return null;
    }

    public List<OrderDto> getKassaOrAdminOrder(User user){
        List<OrderDto> orderDtoList = new ArrayList<>();
        for (Order kassaOrder : orderRepository.findCreatedBy(user.getId())) {
            OrderDto orderDto = new OrderDto();
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

    public ApiResponse<?> loginSuperAdmin(ReqLogin reqLogin){
        User superAdmin = authRepository.findPhoneAndPassword(reqLogin.getPhoneNumber(), reqLogin.getPassword());
        CompanyUserRole companyUserRole = companyUserRoleRepository.findByUserAndRole(superAdmin.getId(), roleRepository.findRoleName(RoleName.ROLE_SUPER_ADMIN).orElseThrow(() -> new ResourceNotFoundException(403, "Role", "roleName", superAdmin)).getId());
        Role role = roleRepository.findId(companyUserRole.getRoleId()).orElseThrow(() -> new ResourceNotFoundException(403, "Role", "id", companyUserRole.getRoleId()));
        if (role.getRoleName().equals(RoleName.ROLE_SUPER_ADMIN)){
            return new ApiResponse<>("success", 200);
        }
        return new ApiResponse<>("Super admin not found", 401);
    }

    public AuthDto addUser(AuthDto authDto) {
        try {
            if (authRepository.equalsUser(authDto.getPhoneNumber(), authDto.getEmail())==null){
                System.out.println("salom");
            }

        }catch (Exception e){
            if (authDto.getPassword().equals(authDto.getPrePassword())) {
                User user = mapper.fromDto(authDto);
                User save = authRepository.save(user);
                return mapper.fromUser(save);
            }
        }
        return null;
    }

}
