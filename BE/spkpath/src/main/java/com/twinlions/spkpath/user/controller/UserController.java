package com.twinlions.spkpath.user.controller;

import com.twinlions.spkpath.consultant.ConsultantDto;
import com.twinlions.spkpath.jwt.TokenDto;
import com.twinlions.spkpath.user.UserDto;
import com.twinlions.spkpath.user.repository.CustomUserDetailsService;
import com.twinlions.spkpath.user.repository.UserRepository;
import com.twinlions.spkpath.user.service.UserService;
import com.twinlions.spkpath.user.vo.UserVO;
import io.swagger.annotations.Api;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@RestController
@RequiredArgsConstructor // 생성자 사용하지 않기 위해
@RequestMapping("/account")
@Api(value = "회원", description = "회원 관련 API 입니다.")
@CrossOrigin(origins = "*", methods = { RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE })
public class UserController {

    private final UserService userService;
    private final UserRepository userRepository;
    private final CustomUserDetailsService customUserDetailsService;
     @Value("${file.path.profilePath}")
     private String profilePath;

    @PostMapping(value = "/signup")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "successful operation"),
            @ApiResponse(responseCode = "400", description = "bad request operation"),
            @ApiResponse(responseCode = "500", description = "SQL Exception")
    })
    @Operation(summary = "회원가입", description = "회원가입을 위한 정보를 입력한다.")
    public ResponseEntity<?> signup(@RequestBody UserDto userDto){
        // TODO : logger 안찍히는 issue -> 해결해야해
//        logger.debug("회원가입요청: {}", userDto.toString()); // 지금 로거 출력 안되는 이슈
        String result = userService.join(userDto);
        // TODO : 회원가입 실패시 예외처리 어떻게 할지 정해야해!
        if(result != null){
            return new ResponseEntity<>(result, HttpStatus.OK);
        }else{
            return new ResponseEntity<Void>(HttpStatus.NOT_ACCEPTABLE);
        }
    }

    @PostMapping(value = "/consultantsignup")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "successful operation"),
            @ApiResponse(responseCode = "400", description = "bad request operation"),
            @ApiResponse(responseCode = "500", description = "SQL Exception")
    })
    @Operation(summary = "상담사 회원가입", description = "상담사 회원가입을 위한 정보를 입력한다.")
    public ResponseEntity<?> csltSignup(@RequestBody ConsultantDto consultantDto){
        // TODO : logger 안찍히는 issue -> 해결해야해
//        logger.debug("회원가입요청: {}", userDto.toString()); // 지금 로거 출력 안되는 이슈
        int result = userService.csltJoin(consultantDto);
        if(result == 1){
            return new ResponseEntity<>("success", HttpStatus.OK);
        } else{
            return new ResponseEntity<>("fail", HttpStatus.OK);
        }
    }

    @PostMapping(value = "/login")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "successful operation"),
            @ApiResponse(responseCode = "400", description = "bad request operation"),
            @ApiResponse(responseCode = "500", description = "SQL Exception")
    })
    @Operation(summary = "로그인", description = "로그인을 위한 정보를 입력한다.")
    public ResponseEntity<?> login(@RequestBody UserDto userDto){
        TokenDto tokenDto = userService.login(userDto.getUserId(), userDto.getUserPwd());
        return new ResponseEntity<>(tokenDto, HttpStatus.OK);
    }

    @GetMapping(value = "/checkid")
    @Operation(summary = "아이디 중복 체크", description = "아이디가 존재하는 아이디인지 확인한다. \n 확인하고자 하는 아이디를 입력한다.")
    public ResponseEntity<String> checkId(@RequestParam String userId){
        int result = userService.checkId(userId);
        if( result == 1){
            return new ResponseEntity<>("success", HttpStatus.OK);
        }else{
            return new ResponseEntity<>("fail", HttpStatus.OK);
        }
    }

    @GetMapping(value = "/checkemail")
    @Operation(summary = "이메일 중복 체크", description = "이메일이 존재하는 이메일인지 확인한다. \n 확인하고자 하는 이메일를 입력한다.")
    public ResponseEntity<String> checkEmail(@RequestParam String userEmail){
        int result = userService.checkEmail(userEmail);
        if( result == 1){
            return new ResponseEntity<>("success", HttpStatus.OK);
        }else{
            return new ResponseEntity<>("fail", HttpStatus.OK);
        }
    }

    @PostMapping(value = "/mypage")
    @Operation(summary = "내 프로필 조회", description = "내 프로필을 조회한다.")
    public ResponseEntity<?> readProfile(@RequestBody UserVO userId){
        Optional<?> user = userService.mypage(userId.getUserId());
        return new ResponseEntity<>(user.get(), HttpStatus.OK);
    }

    /**
     * 수정할 정보를 userDto 에 담아 보내주면 수정한다.
     * 현재는 pwd, info, phone 정보만 수정 가능하다.
     * @param userDto
     * @return
     */
    @PutMapping(value = "/change")
    @Operation(summary = "내 프로필 정보 수정", description = "내 프로필의 정보를 수정할 수 있습니다.")
    public ResponseEntity<?> changeProfile(@RequestBody UserDto userDto){
        return new ResponseEntity<>(userService.update(userDto), HttpStatus.OK);
    }

    @PostMapping(value = "/profile")
    @Operation(summary = "프로필 사진 업로드", description = "내 프로필 사진을 업로드한다.")
    public ResponseEntity<?> uploadProfile(
            @RequestParam("userId") String userId, @RequestParam("file")MultipartFile file){
        if(!file.isEmpty()) {
            LocalDate now = LocalDate.now();
            String saveFolder = profilePath + "/" + userId + "/" + now;
            File folder = new File(saveFolder);
            if (!folder.exists())
                folder.mkdirs();

            String originalFileName = file.getOriginalFilename();

            if (!originalFileName.isEmpty()) {
                String saveFileName = UUID.randomUUID().toString()
                        + originalFileName.substring(originalFileName.lastIndexOf('.'));
                try {
                    file.transferTo(new File(folder, saveFileName));
                    userService.uploadProfile(userId, userId + "/" + now + "/" + saveFileName);
                    log.info("UserController:: upload profile {} at {}", userId, saveFolder);
                    return new ResponseEntity<String>(userId + "/" + now + "/"+ saveFileName, HttpStatus.CREATED);
                } catch (Exception e) {
                    e.printStackTrace();
                    return new ResponseEntity<Void>(HttpStatus.BAD_REQUEST);
                }
            }
        }
        return new ResponseEntity<Void>(HttpStatus.BAD_REQUEST);
    }
}