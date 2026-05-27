package com.car.serviceimpl;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Map;
import java.util.Optional;
import java.util.Random;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.car.dto.AddUserDto;
import com.car.dto.EmailOtpVerifyDto;
import com.car.entity.Car;
import com.car.entity.User;
import com.car.enums.CarStatus;
import com.car.enums.UserStatus;
import com.car.events.SimpleMessageEvent;
import com.car.mapping.CustomModelMapper;
import com.car.repository.UserRepository;
import com.car.service.MailService;
import com.car.service.UserService;
import com.car.util.EmailMessageBuilderUtil;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

	private final UserRepository userRepo;
	
	private final CustomModelMapper customMapper;
	
	private final Random random;
	
	private final EmailMessageBuilderUtil builderUtil;
	
	//private final MailService mailService;
	
	private final ApplicationEventPublisher eventPublisher;
	
	@Qualifier("otpholder")
	private final Map<String, Object[]> otpHolder;
	
	private final PasswordEncoder encoder;
	
	
	@Override
	public String initiateUserVerification(AddUserDto userDto) {
		Integer otp=random.nextInt(100000, 999999);
		String otpMessage=builderUtil.emailMessageBuilder(userDto.getName(), otp);
		SimpleMessageEvent messageEvent=SimpleMessageEvent.builder()
				.receiverEmail(userDto.getEmail())
				.subject(otpMessage)
				.message("VELOCITY registration otp")
				.build();
		eventPublisher.publishEvent(messageEvent); //culprit
		Object[] tempOtp= {otp,LocalDateTime.now().plusMinutes(2),userDto};
		otpHolder.put(userDto.getEmail(), tempOtp);
		return "Otp sent to "+userDto.getEmail();
	}


	@Override
	public String finalUserVerificationService(EmailOtpVerifyDto otpDto) {
		Object[] tempUserData=otpHolder.get(otpDto.getEmail());
		if(tempUserData==null) throw new RuntimeException("Invalid email id");
		LocalDateTime currentTime=LocalDateTime.now();
		LocalDateTime expiryTime=(LocalDateTime)tempUserData[1];
		if(currentTime.isAfter(expiryTime)) throw new RuntimeException("Session timed out, Try Again");
		Integer inMemoryOtp=(Integer)tempUserData[0];
		Integer userOtp=otpDto.getOtp();
		if(!inMemoryOtp.equals(userOtp)) throw new RuntimeException("Invalid Otp");
		AddUserDto userDto=(AddUserDto)tempUserData[2];
		User user=customMapper.addUserDtoToUserEntity(userDto);
		userRepo.save(user);
		String registrationMail=builderUtil.userRegistrationSuccessBuilder(userDto.getName());
		SimpleMessageEvent messageEvent=SimpleMessageEvent.builder()
				.receiverEmail(userDto.getEmail())
				.subject(registrationMail)
				.message("Welcome to VELOCITY")
				.build();
		eventPublisher.publishEvent(messageEvent);
		return userDto.getName()+" saved successfully";
	}


	@Override
	public String resendOtpService(String email) {
		Integer otp=random.nextInt(100000, 999999);
		String otpMessage=builderUtil.emailMessageBuilder(email, otp);
		SimpleMessageEvent messageEvent=SimpleMessageEvent.builder()
				.receiverEmail(email)
				.subject(otpMessage)
				.message("VELOCITY registration otp")
				.build();
		String singleKey=otpHolder.keySet().iterator().next();
		//Object[] tempUserData=otpHolder.get(singleKey);
		if(!email.equals(singleKey)) throw new RuntimeException("Invalid email id");
		eventPublisher.publishEvent(messageEvent);
		//Object[] tempOtp= {otp,LocalDateTime.now().plusMinutes(2),
		return "otp sent successfully";
	}


	@Override
	@Transactional
	public String updateUserByIdService(String email, AddUserDto userDto) {
		User user=userRepo.findByEmail(email).orElseThrow(()-> new RuntimeException("User Not Found"));
		if(user.getStatus()==UserStatus.INACTIVE) throw new RuntimeException("Invalid user id");
		user.setEmail(userDto.getEmail());
		user.setName(userDto.getName());
		user.setPhone(userDto.getPhone());
		user.setPassword(encoder.encode(userDto.getPassword()));
		user=userRepo.save(user);
		return "User with name "+userDto.getName()+" has been updated successfully";
	}

	@Override
	public User getUserByIdService(Integer id) {
		User user=userRepo.findById(id).orElseThrow(()-> new RuntimeException("Invalid id"));
		if(user.getStatus()==UserStatus.INACTIVE) throw new RuntimeException("Invalid user id");
		return user;
	}
	

}
