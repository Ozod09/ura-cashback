package itca.uz.ura_cashback_2.controller;

import itca.uz.ura_cashback_2.entity.Order;
import itca.uz.ura_cashback_2.payload.*;
import itca.uz.ura_cashback_2.repository.OrderRepository;
import itca.uz.ura_cashback_2.service.OrderService;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

@RestController
@CrossOrigin
@RequestMapping("/api/order")
public class OrderController {
    final OrderService orderService;
    final OrderRepository repository;

    public OrderController(OrderService orderService,OrderRepository repository) {
        this.orderService = orderService;
        this.repository = repository;
    }


    @GetMapping("/list")
    public HttpEntity<?> getOrderList() {
        return ResponseEntity.ok(orderService.getOrderList());
    }

    @GetMapping("/{id}")
    public HttpEntity<?> getOrderFindByUser(@PathVariable Long id) {
        return ResponseEntity.ok(orderService.getFindByUser(id));
    }


//    @PutMapping("statistic/{id}")
//    public HttpEntity<?> getStatistic(@PathVariable Long id){
//        return ResponseEntity.ok(orderService.getStatisticList(id));
//    }

    @PostMapping("/statistic")
    public HttpEntity<?> getStatistic( @RequestBody ReqStatistic reqStatistic){



        return ResponseEntity.ok(orderService.getStatistic(reqStatistic));
    }

    @PostMapping
    public HttpEntity<?> addOrder(@RequestBody OrderDto orderDto) {
        ApiResponse<?> apiResponse = orderService.addOrder(new Order(), orderDto);
        return ResponseEntity.status(apiResponse.isSuccess() ? HttpStatus.CREATED : HttpStatus.CONFLICT).body(apiResponse);
    }

    @DeleteMapping("/{id}")
    public HttpEntity<?> deleteOrder(@PathVariable Long id) {
        return ResponseEntity.ok(orderService.deleteOrder(id));
    }

    //xato
    @PutMapping("/login")
    public HttpEntity<?> isLogin(@RequestBody ReqLogin loginDto) {
        return ResponseEntity.ok(orderService.login(loginDto));
    }

}
