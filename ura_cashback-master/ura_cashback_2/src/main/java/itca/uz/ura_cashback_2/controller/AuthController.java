package itca.uz.ura_cashback_2.controller;

import itca.uz.ura_cashback_2.entity.User;
import itca.uz.ura_cashback_2.payload.ApiResponse;
import itca.uz.ura_cashback_2.payload.AuthDto;
import itca.uz.ura_cashback_2.payload.ReqLogin;
import itca.uz.ura_cashback_2.payload.ReqPassword;
import itca.uz.ura_cashback_2.repository.AuthRepository;
import itca.uz.ura_cashback_2.service.AuthService;
import itca.uz.ura_cashback_2.utils.AppConstant;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.ResourceAccessException;


@RestController
@RequestMapping(path = "/api/auth")
@CrossOrigin
public class AuthController {

    final AuthService authService;
    final AuthRepository authRepository;


    public AuthController(AuthService authService, AuthRepository authRepository) {
        this.authService = authService;
        this.authRepository = authRepository;
    }

    @PostMapping
    public HttpEntity<?> addAuth(@RequestBody AuthDto authDto){
        ApiResponse<?> apiResponse = authService.addRegisterClient(authDto);
        return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);
    }

    @PostMapping("/companyAdmin")
    public HttpEntity<?> addCompanyAdmin(@RequestBody AuthDto authDto){
        return ResponseEntity.ok(authService.addCompanyAdmin(authDto));
    }

    @PostMapping("/companyKassa")
    public HttpEntity<?> addCompanyKassa(@RequestBody AuthDto authDto){
            return ResponseEntity.ok(authService.addKassa(authDto));
    }

    @PostMapping("/company/login")
    public HttpEntity<?> loginCompany(@RequestBody ReqLogin reqLogin){
        return ResponseEntity.ok( authService.loginCompany(reqLogin));
    }

    @PostMapping("/superAdmin/login")
    public HttpEntity<?> loginSuperAdmin(@RequestBody ReqLogin reqLogin){
        return ResponseEntity.ok(authService.loginSuperAdmin(reqLogin));
    }
    @PostMapping("/admin/password")
    public HttpEntity<?> passwordEdit(@RequestBody ReqPassword reqPassword){
        return ResponseEntity.ok(authService.editPassword(reqPassword));
    }


    @PutMapping("/companyAdmin/{id}")
    public HttpEntity<?> editCompanyAdmin(@PathVariable Long id, @RequestBody AuthDto authDto){
        User user = authRepository.findById(id).orElseThrow(() -> new ResourceAccessException("getUser"));
        return ResponseEntity.ok(authService.editCompanyAdmin(authDto, user));
    }

    @PutMapping("/companyKassa/{id}")
    public HttpEntity<?> editCompanyKassa(@PathVariable Long id, @RequestBody AuthDto authDto){
        User user = authRepository.findById(id).orElseThrow(() -> new ResourceAccessException("getUser"));
        return ResponseEntity.ok(authService.editKassa(authDto, user));
    }

    @DeleteMapping("/{id}")
    public HttpEntity<?> deleteAuth(@PathVariable Long id){
        return ResponseEntity.ok(authService.deleteClient(id));
    }


    @GetMapping
    public HttpEntity<?> getUserPage(@RequestParam(value = "page",defaultValue = AppConstant.DEFAULT_PAGE) int page ,
                                     @RequestParam(value = "size",defaultValue = AppConstant.DEFAULT_SIZE) int size) throws Exception {
       return ResponseEntity.ok(authService.getUserList(page,size));
    }


    @GetMapping("/{id}")
    public HttpEntity<?> getOneUser(@PathVariable Long id) {
        return ResponseEntity.ok(authService.getOneUser(id));
    }



    @PutMapping("/active/{id}")
    public HttpEntity<?> activeUser(@PathVariable Long id){
        return ResponseEntity.ok(authService.activeUser(id));
    }


    @GetMapping("/order/{phoneNumber}")
    public HttpEntity<?> findByPhoneNumber(@PathVariable String phoneNumber) {
        return ResponseEntity.ok(authService.findByPhoneNumber(phoneNumber));
    }

}
