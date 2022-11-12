package itca.uz.ura_cashback_2.service;

import itca.uz.ura_cashback_2.entity.Company;
import itca.uz.ura_cashback_2.entity.CompanyUserRole;
import itca.uz.ura_cashback_2.entity.Order;
import itca.uz.ura_cashback_2.entity.User;
import itca.uz.ura_cashback_2.entity.enums.RoleName;
import itca.uz.ura_cashback_2.exception.ResourceNotFoundException;
import itca.uz.ura_cashback_2.payload.ApiResponse;
import itca.uz.ura_cashback_2.payload.OrderDto;
import itca.uz.ura_cashback_2.payload.ReqLogin;
import itca.uz.ura_cashback_2.payload.Statistic;
import itca.uz.ura_cashback_2.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.ResourceAccessException;

import java.util.ArrayList;
import java.util.List;

@Service
public class OrderService {
    final OrderRepository orderRepository;
    final AuthRepository authRepository;
    final CompanyRepository companyRepository;
    final CompanyUserRoleService companyUserRoleService;
    final RoleRepository roleRepository;
    final AuthService authService;
    final CompanyUserRoleRepository companyUserRoleRepository;

    public OrderService(OrderRepository orderRepository, AuthRepository authRepository, CompanyRepository companyRepository,
                        CompanyUserRoleService companyUserRoleService, RoleRepository roleRepository, AuthService authService, CompanyUserRoleRepository companyUserRoleRepository) {
        this.orderRepository = orderRepository;
        this.authRepository = authRepository;
        this.companyRepository = companyRepository;
        this.companyUserRoleService = companyUserRoleService;
        this.roleRepository = roleRepository;
        this.authService = authService;
        this.companyUserRoleRepository = companyUserRoleRepository;
    }

    public ApiResponse<?> addOrder(Order order, OrderDto orderDto) {
        int cashback = orderDto.getCashback(), cash_price = orderDto.getCash_price();
        User getClient = authService.getOneUser(orderDto.getClientId());
        User getAdmin = authService.getOneUser(orderDto.getAdminId());
        Company getCompany;
        try {
            getCompany = companyUserRoleService.getCompanyFindByUser(getAdmin.getId(), roleRepository.findRoleName(RoleName.ROLE_ADMIN).orElseThrow(() -> new ResourceNotFoundException(403, "Role", "roleName", getAdmin)).getId());
        } catch (Exception e) {
            getCompany = companyUserRoleService.getCompanyFindByUser(getAdmin.getId(), roleRepository.findRoleName(RoleName.ROLE_KASSA).orElseThrow(() -> new ResourceNotFoundException(403, "Role", "roleName", getAdmin)).getId());
        }
        if (cashback <= getClient.getSalary()) {
            order.setCashback((((cash_price - cashback) / 100) * getCompany.getClientPercentage()));
            int salary = cashback == 0
                    ? getClient.getSalary() + ((cash_price / 100) * getCompany.getClientPercentage())
                    : (getClient.getSalary() - cashback) + ((((cash_price - cashback) / 100) * getCompany.getClientPercentage()));
            authService.editUserSalary(salary);
        } else {
            return new ApiResponse<>("There are not enough funds in your Cashback account", 401);
        }
        order = Order.builder()
                .client(getClient)
                .cash_price(cash_price).build();
        order.setCreatedBy(getAdmin.getId());
        orderRepository.save(order);
        return new ApiResponse<>("successfully saved order", 200);
    }

    public User login(ReqLogin reqLogin) {
        User user = authRepository.findPhoneAndPassword(reqLogin.getPhoneNumber(), reqLogin.getPassword());
        CompanyUserRole companyUserRole = companyUserRoleRepository.findByUserAndRole(user.getId(), roleRepository.findRoleName(RoleName.ROLE_KASSA).orElseThrow(() -> new ResourceNotFoundException(403, "Role", "roleName", user)).getId());
        CompanyUserRole companyUserRole1 = companyUserRoleRepository.findByUserAndRole(user.getId(), roleRepository.findRoleName(RoleName.ROLE_ADMIN).orElseThrow(() -> new ResourceNotFoundException(403, "Role", "roleName", user)).getId());
        if (companyUserRole != null || companyUserRole1 != null) {
            return user;
        }
        return null;
    }

    public Order getOneOrder(Long id) {
        return orderRepository.findById(id).orElseThrow(() -> new ResourceAccessException("getOrder"));
    }

    public List<Statistic> getStatisticList(Long companyId) {
        return getOrder(getAdminId(companyId), new ArrayList<>());
    }

    public List<Statistic> getOrder(List<Long> userIdList, List<Statistic> statisticList) {
        for (Long adminId : userIdList)
            getStatistic(orderRepository.findCreatedBy(adminId), statisticList);
        return statisticList;
    }

    public void getStatistic(List<Order> order, List<Statistic> statisticList) {
        for (Order onrOrder : order) findByDate(onrOrder, statisticList);
    }

    public void findByDate(Order order, List<Statistic> statisticList) {
        Statistic statistic = Statistic.builder()
                .id(order.getId())
                .admin(authService.getOneUser(order.getCreatedBy()))
                .cash_price(order.getCash_price())
                .cashback(order.getCashback())
                .user(order.getClient())
                .build();
        statisticList.add(statistic);
    }

    public List<Long> getAdminId(Long companyId) {
        return companyUserRoleRepository.getCompanyRole(companyId,
                roleRepository.findRoleName(RoleName.ROLE_ADMIN).orElseThrow(() ->
                        new ResourceNotFoundException(403, "Role", "role Admin", companyId)).getId(),
                roleRepository.findRoleName(RoleName.ROLE_SUPER_ADMIN).orElseThrow(() ->
                        new ResourceNotFoundException(403, "Role", "role Super Admin", companyId)).getId(),
                roleRepository.findRoleName(RoleName.ROLE_KASSA).orElseThrow(() ->
                        new ResourceNotFoundException(403, "Role", "role Kasser", companyId)).getId());
    }

    public List<Order> getFindByUser(Long userId) {
        return orderRepository.findCreatedBy(userId);
    }

    public List<Order> getOrderList() {
        return orderRepository.findAll();
    }

    public ApiResponse<?> deleteOrder(Long orderId) {
        orderRepository.deleteById(orderId);
        return new ApiResponse<>("successfully deleted Order", 200);
    }
}
