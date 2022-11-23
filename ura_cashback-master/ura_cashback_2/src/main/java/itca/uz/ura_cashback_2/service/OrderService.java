package itca.uz.ura_cashback_2.service;

import itca.uz.ura_cashback_2.entity.Company;
import itca.uz.ura_cashback_2.entity.CompanyUserRole;
import itca.uz.ura_cashback_2.entity.Order;
import itca.uz.ura_cashback_2.entity.User;
import itca.uz.ura_cashback_2.entity.enums.RoleName;
import itca.uz.ura_cashback_2.exception.ResourceNotFoundException;
import itca.uz.ura_cashback_2.payload.*;
import itca.uz.ura_cashback_2.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.ResourceAccessException;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.*;

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
        int cashback = orderDto.getCashback(), cash_price = orderDto.getCash_price(), companyClientCash;
        User getClient = authService.getOneUser(orderDto.getClientId());
        User getAdmin = authService.getOneUser(orderDto.getAdminId());
        Company getCompany;
        try {
            getCompany = companyUserRoleService.getCompanyFindByUser(getAdmin.getId(),2);
        } catch (Exception e) {
            getCompany = companyUserRoleService.getCompanyFindByUser(getAdmin.getId(), 3);
        }
        if (cashback <= getClient.getSalary()) {
            companyClientCash = ((((cash_price - cashback) / 100) * getCompany.getClientPercentage()));
            int salary = cashback == 0
                    ? getClient.getSalary() + ((cash_price / 100) * getCompany.getClientPercentage())
                    : (getClient.getSalary() - cashback) + ((((cash_price - cashback) / 100) * getCompany.getClientPercentage()));
            authService.editUserSalary(getClient, salary);
        } else {
            return new ApiResponse<>("There are not enough funds in your Cashback account", 401);
        }
        order = Order.builder()
                .client(getClient)
                .companyId(companyUserRoleRepository.getKassir(getAdmin.getId(), 3).orElseThrow(() -> new ResourceNotFoundException(403, "companyUserRole", "id", getAdmin)).getCompanyId())
                .clientCompCash(cashback)
                .cash_price(cash_price).build();
        order.setCreatedBy(getAdmin.getId());
        order.setCompanyClientCash(companyClientCash);
        orderRepository.save(order);
        return new ApiResponse<>("successfully saved order", 200);
    }

//    public User login(ReqLogin reqLogin) {
//        User user = authRepository.findPhoneAndPassword(reqLogin.getPhoneNumber(), reqLogin.getPassword()).orElseThrow(() -> new ResourceNotFoundException(404, "User", "id", reqLogin));
//        CompanyUserRole companyUserRole = companyUserRoleRepository.getKassir(user.getId(), roleRepository.findRoleName(RoleName.ROLE_KASSA).orElseThrow(() -> new ResourceNotFoundException(403, "Role", "roleName", user)).getId()).orElseThrow(() -> new ResourceNotFoundException(404, "companyUserRole", "id", reqLogin));
//        CompanyUserRole companyUserRole1 = companyUserRoleRepository.getKassir(user.getId(), roleRepository.findRoleName(RoleName.ROLE_ADMIN).orElseThrow(() -> new ResourceNotFoundException(403, "Role", "roleName", user)).getId()).orElseThrow(() -> new ResourceNotFoundException(404, "companyUserRole", "id", reqLogin));
//        if (companyUserRole != null || companyUserRole1 != null) {
//            return user;
//        }
//        return null;
//    }

    public Order getOneOrder(Long id) {
        return orderRepository.findById(id).orElseThrow(() -> new ResourceAccessException("getOrder"));
    }

//    public List<Statistic> getStatisticList(Long companyId) {
//        List<Statistic> statisticList = new ArrayList<>();
//        List<Long> userIdList = getAdminId(companyId);
//        return getOrder(userIdList, statisticList);
//    }

//    public List<Statistic> getOrder(List<Long> userIdList, List<Statistic> statisticList) {
//        for (Long adminId : userIdList) {
//            List<Order> orderList = orderRepository.findCreatedBy(adminId);
//            getStatistic(orderList, statisticList);
//        }
//        return statisticList;
//    }

//    public void getStatistic(List<Order> order, List<Statistic> statisticList) {
//        for (Order onrOrder : order) {
//            Statistic statistic = Statistic.builder()
//                    .admin(authService.getOneUser(onrOrder.getCreatedBy()))
//                    .id(onrOrder.getId())
//                    .cash_price(onrOrder.getCash_price())
//                    .cashback(onrOrder.getCompanyClientCash())
//                    .user(onrOrder.getClient())
//                    .build();
//            statisticList.add(statistic);
//        }
//    }

//    public List<Long> getAdminId(Long companyId) {
//        return companyUserRoleRepository.getCompanyRole
//                (companyId, roleRepository.findRoleName(RoleName.ROLE_ADMIN).orElseThrow(() ->
//                                new ResourceNotFoundException(403, "Role", "role Admin", companyId)).getId(),
//                        roleRepository.findRoleName(RoleName.ROLE_SUPER_ADMIN).orElseThrow(() ->
//                                new ResourceNotFoundException(403, "Role", "role Super Admin", companyId)).getId(),
//                        roleRepository.findRoleName(RoleName.ROLE_KASSA).orElseThrow(() ->
//                                new ResourceNotFoundException(403, "Role", "role Kasseer", companyId)).getId());
//    }

    public List<Order> getFindByUser(Long userId) {
        return orderRepository.findCreatedBy(userId);
    }

    public List<OrderDto> getOrderList() {
        List<OrderDto> orderDtoList = new ArrayList<>();
        for(Order order : orderRepository.findAll()){
            OrderDto orderDto = new OrderDto();
            orderDto.setClient(authRepository.findById(order.getClient().getId()).get());
            orderDto.setAdmin(authRepository.findById(order.getCreatedBy()).get());
            orderDto.setCashback(order.getCash_price());
            orderDto.setCash_price(order.getCash_price());
            orderDto.setCompany(companyRepository.findById(order.getCompanyId()).get());
            orderDtoList.add(orderDto);
        }
        return orderDtoList;
    }

    public ApiResponse<?> deleteOrder(Long orderId) {
        orderRepository.deleteById(orderId);
        return new ApiResponse<>("successfully deleted Order", 200);
    }

    public ResStatistic getStatistic(ReqStatistic reqStatistic) {
        Optional<Company> company = companyRepository.findById(reqStatistic.getCompanyId());
        if (company.isPresent()) {
            List<Order> orderList = orderRepository.companyOrder(reqStatistic.getCompanyId());
            Set<Long> userCount = new HashSet<>();
            int allBalance = 0;
            int clientNaqtTulovComp = 0;
            int clientCompCash = 0;
            int companyClientCash = 0;
            int clientCash = 0;
            for (Order order : orderList) {
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy/MM");
                if(simpleDateFormat.format(order.getCreatedAt()).equals(simpleDateFormat.format(reqStatistic.getFilterDate()))){
                    userCount.add(order.getClient().getId());
                    allBalance+=order.getCash_price();
                    clientCompCash+=order.getClientCompCash();
                    companyClientCash+=order.getCompanyClientCash();
                    clientCash+=order.getClient().getSalary();
                }
            }
            return ResStatistic.builder()
                    .jamiClient(userCount.size())
                    .allBalance(allBalance)
                    .clientNaqtTulovComp(clientNaqtTulovComp)
                    .clientCompCash(clientCompCash)
                    .companyClientCash(companyClientCash)
                    .clientCash(clientCash)
                    .build();
        }
        return null;
    }

}
