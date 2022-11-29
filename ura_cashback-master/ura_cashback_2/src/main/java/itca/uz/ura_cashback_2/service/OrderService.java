package itca.uz.ura_cashback_2.service;

import itca.uz.ura_cashback_2.entity.Company;
import itca.uz.ura_cashback_2.entity.CompanyUserRole;
import itca.uz.ura_cashback_2.entity.Order;
import itca.uz.ura_cashback_2.entity.User;
import itca.uz.ura_cashback_2.exception.ResourceNotFoundException;
import itca.uz.ura_cashback_2.payload.*;
import itca.uz.ura_cashback_2.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.ResourceAccessException;

import java.sql.Timestamp;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class OrderService {
    private final OrderRepository orderRepository;
    private final AuthRepository authRepository;
    private final CompanyRepository companyRepository;
    private final CompanyUserRoleService companyUserRoleService;
    private final AuthService authService;
    private final CompanyUserRoleRepository companyUserRoleRepository;



    public ApiResponse<?> addOrder(OrderDto orderDto) {
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
        Order order = Order.builder()
                .client(getClient)
                .companyId(companyUserRoleRepository.getKassir(getAdmin.getId(), 2).orElseThrow(() -> new ResourceNotFoundException(403, "companyUserRole", "id", getAdmin)).getCompanyId())
                .clientCompCash(cashback)
                .cash_price(cash_price).build();
        order.setCreatedBy(getAdmin.getId());
        order.setCompanyClientCash(companyClientCash);
        orderRepository.save(order);
        return new ApiResponse<>("successfully saved order", 200);
    }

    public User login(ReqLogin reqLogin) {
        User user = authRepository.findPhoneAndPassword(reqLogin.getPhoneNumber(), reqLogin.getPassword()).orElseThrow(() -> new ResourceNotFoundException(404, "User", "id", reqLogin));
        CompanyUserRole companyUserRole = companyUserRoleRepository.kassir(user.getId()).orElseThrow(() -> new ResourceNotFoundException(404, "companyUserRole", "id", reqLogin));
        if (companyUserRole != null ) {
            return user;
        }
        return null;
    }

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

    public List<Order> getOrderList() {
        return orderRepository.findAll();
    }

    public ApiResponse<?> deleteOrder(Long orderId) {
        orderRepository.deleteById(orderId);
        return new ApiResponse<>("successfully deleted Order", 200);
    }

    public ResStatistic getStatistic(ReqStatistic reqStatistic) {
        Optional<Company> company = companyRepository.findById(reqStatistic.getCompanyId());
        if (company.isPresent()) {
            Timestamp startTime = Timestamp.valueOf(reqStatistic.getStartTime());
            Timestamp endTime = Timestamp.valueOf(reqStatistic.getFinishTime());
            List<Order> orderList = orderRepository.findByCompanyIdAndCreatedAt(reqStatistic.getCompanyId(),startTime,endTime);
            Set<Long> allClient = new HashSet<>();
            int allBalance = 0;
            int companyClientCash = 0;
            int clientCash  = 0;
            for (Order order1 : orderList) {
                allBalance+=order1.getCash_price();
                companyClientCash+=order1.getCompanyClientCash();
                allClient.add(order1.getClient().getId());
            }
            for(Long id : allClient){
                clientCash += authRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException(404,"User","id",id)).getSalary();
            }
            int urtachaCheck = allBalance/ allClient.size();
            return ResStatistic.builder()
                    .jamiClient(allClient.size())
                    .allBalance(allBalance)
                    .companyClientCash(companyClientCash)
                    .clientCash(clientCash)
                    .urtachaCheck(urtachaCheck)
                    .build();
        }
        return null;
    }
}
