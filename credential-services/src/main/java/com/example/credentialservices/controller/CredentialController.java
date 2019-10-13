package com.example.credentialservices.controller;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Optional;

import com.example.credentialservices.entity.Credential;
import com.example.credentialservices.repository.CredentialRepository;

//import com.nexmo.client.NexmoClientException;
//import com.nexmo.client.sms.SmsSubmissionResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@CrossOrigin("*")
@RestController
@Api(value = "Authorization", description = "Auth service operations on Clients", tags = ("Auth"))
public class CredentialController {

  @Autowired
  private CredentialRepository credentialRepository;

  @RequestMapping(path = "/login/user", method = RequestMethod.POST)
  @ApiOperation(value = "Authenticate user", notes = "Authenticate username and password on sign in", nickname = "validateUser")
  public Credential validateUser(@RequestBody Credential c) {
    
    SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss");
    Optional<Credential> optional = credentialRepository.findByUsername(c.getUsername());
    if (!optional.isPresent()) return null;
    Credential credential = optional.get();

    if (c.getPassword().equals(credential.getPassword())) {
      log.info("Credentials are correct");

      // Generate HashCode from username, password and date:
      Date date = new Date(System.currentTimeMillis());
      String hashcode = new String();
      hashcode = credential.getUsername() + credential.getPassword() + formatter.format(date);
      credential.setHashcode(hashcode.hashCode());

      // Declare user has logged in
      credential.setStatus(1);
      credentialRepository.save(credential);

      Credential returnedCredential = new Credential();
      returnedCredential.setUsername(credential.getUsername());
      returnedCredential.setRole(credential.getRole());
      returnedCredential.setStatus(credential.getStatus());
      returnedCredential.setHashcode(credential.getHashcode());

      return returnedCredential;
    } else {
      log.info("Username and Password are not correct");
      return null;
    }
  }

  // @GetMapping("/auth/authState")
  // public @ResponseBody int authenticateUser(@RequestParam(name = "code") String hashcode)
  //     throws IOException, NexmoClientException {
  //   return credentialService.isAuthenticated(Integer.parseInt(hashcode));
  // }

  // @GetMapping("/logout")
  // public @ResponseBody boolean logout(@RequestParam(name = "code") String hashcode) {
  //   return credentialService.logout(Integer.parseInt(hashcode));
  // }

  // @PostMapping
  // @RequestMapping("/sendValidationCode")
  // public SmsSubmissionResponse sendValidationCode(@RequestBody Client client) throws IOException, NexmoClientException {
  //   int validationCode = credentialService.getValidationCode();
    
  //   Client c = clientService.findClientAndCredentialAssociatedByClientId(client.getClientId());
  //   clientService.updateValidationCode(c, validationCode);
  //   String cellphone = c.getContactInfo().getCellphone();
    
  //   return smsService.sendValidationCode(cellphone, Integer.toString(validationCode));
  // }

  // @PostMapping
  // @RequestMapping("/validateCode")
  // public boolean validateValidationCode(@RequestBody Client data){
  //   int validationCode = data.getValidationCode();
  //   String clientId = data.getClientId();
  //   boolean isValidated = credentialService.isValidationCodeCorrect(validationCode, clientId);
  //   if(isValidated) {
  //     Client client = clientService.findByClientId(clientId);
  //     clientService.updateValidationCode(client, 0);
  //   }

  //   return isValidated;
  // }

  // @PostMapping
  // @RequestMapping("/setCredential")
  // public boolean setCredential(@RequestBody Credential data){
  //   String clientId = data.getRole(); // Field 'role' used to transfer 'clientId' from client-side
  //   Client client = clientService.findByClientId(clientId);
  //   if( credentialDao.findByUsername(data.getUsername()) != null ) return false;
    
  //   Credential credential = credentialService.setCredential(client, data.getUsername(), data.getPassword(), data.getMail());
  //   if (credential == null) return false;

  //   return true;
  // }
  
  // @ResponseStatus(HttpStatus.NOT_FOUND)
  // @ExceptionHandler(NotFoundException.class)
  // public void handleNotFound(Exception e) {
  //   log.info("Not found exception, " + e.getMessage());
  // }

  
}