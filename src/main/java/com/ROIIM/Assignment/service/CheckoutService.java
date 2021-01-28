package com.ROIIM.Assignment.service;

import com.ROIIM.Assignment.dto.CreateNewCustomerDTOs.CreateNewCustomerRequestDTO;
import com.ROIIM.Assignment.dto.CreateNewCustomerDTOs.CreateNewCustomerResponseDTO;
import com.ROIIM.Assignment.dto.MakePaymentDTOs.MakePaymentRequestDTO;
import com.ROIIM.Assignment.dto.MakePaymentDTOs.MakePaymentResponseDTO;
import com.ROIIM.Assignment.dto.SingleUseCustomerTokenDTOs.SingleUseCustomerTokenRequestDTO;
import com.ROIIM.Assignment.dto.SingleUseCustomerTokenDTOs.SingleUseCustomerTokenResponseDTO;
import com.ROIIM.Assignment.entity.Customer;
import com.ROIIM.Assignment.model.DateOfBirth;
import com.ROIIM.Assignment.repository.CustomerRepository;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.Random;

@Service
public class CheckoutService {

    private CustomerRepository customerRepository;

    private RestTemplate restTemplate;

    private String baseurl;

    private String APIKeyId;

    private String APIKeyPassword;

    private Random rd;

    public CheckoutService(){}

    @Autowired
    public CheckoutService(CustomerRepository customerRepository, RestTemplate restTemplate ){

        this.customerRepository = customerRepository;
        this.restTemplate = restTemplate;
        baseurl = "https://api.test.paysafe.com/paymenthub/v1";
        APIKeyId = "private-7751";
        APIKeyPassword = "B-qa2-0-5f031cdd-0-302d0214496be84732a01f690268d3b8eb72e5b8ccf94e2202150085913117f2e1a8531505ee8ccfc8e98df3cf1748";
        rd = new Random();
    }


    public SingleUseCustomerTokenResponseDTO creatSingleUserCustomerToken( String emailId ){

        Customer customer = null;

        customer = customerRepository.findByEmail( emailId );

        // check if user is previously registered
        if( customer == null ){

            // create a new user
            customer = createCustomer( emailId );

            // check if user is created or not
            if( customer == null ){

                return null;
            }
        }

        // get API url in the string
        String url = baseurl + "/customers/" + customer.getPaysafeId() + "/singleusecustomertokens" ;

        // create new http header and set content type to application/json
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));

        // set basic authorization with api key and its value
        headers.setBasicAuth( APIKeyId, APIKeyPassword );

        // create request object
        SingleUseCustomerTokenRequestDTO singleUseCustomerTokenRequestDTO = new SingleUseCustomerTokenRequestDTO();
        singleUseCustomerTokenRequestDTO.setMerchantRefNum( String.valueOf( rd.nextInt() ) );
        singleUseCustomerTokenRequestDTO.setPaymentTypes( Arrays.asList( "CARD" ) );

        // convert request object in to json object
        ObjectMapper objectMapper = new ObjectMapper();
        String jsonString = null;
        try {

            jsonString = objectMapper.writeValueAsString( singleUseCustomerTokenRequestDTO );
        }
        catch (JsonMappingException e) {
            e.printStackTrace();
        }
        catch (JsonGenerationException e) {
            e.printStackTrace();
        }
        catch (IOException e) {
            e.printStackTrace();
        }

        // build the request
        HttpEntity< String > entity = new HttpEntity<String>( jsonString, headers );

        // send POST request
        ResponseEntity< SingleUseCustomerTokenResponseDTO > responseEntity = restTemplate.postForEntity( url, entity, SingleUseCustomerTokenResponseDTO.class );

        if( responseEntity.getStatusCode().equals( HttpStatus.CREATED ) ) {


            SingleUseCustomerTokenResponseDTO singleUseCustomerTokenResponseDTO = responseEntity.getBody();
            singleUseCustomerTokenResponseDTO.setMerchantRefNum(singleUseCustomerTokenRequestDTO.getMerchantRefNum());
            System.out.println(responseEntity.getBody());
            return responseEntity.getBody();
        }

        return null;
    }

    public Customer createCustomer( String emailId ){

        // get API url in the string
        String url = baseurl + "/customers";

        // create new http header and set content type to application/json
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));

        // set basic authorization with api key and its value
        headers.setBasicAuth( APIKeyId, APIKeyPassword );

        // create a new map for the body of the request and put all the values received from the user in the map
        CreateNewCustomerRequestDTO createNewCustomerRequestDTO = new CreateNewCustomerRequestDTO();
        createNewCustomerRequestDTO.setMerchantCustomerId( String.valueOf( rd.nextInt() ) );
        createNewCustomerRequestDTO.setLocale( "en_US" );
        createNewCustomerRequestDTO.setFirstName( "abc" );
        createNewCustomerRequestDTO.setMiddleName( "pqr" );
        createNewCustomerRequestDTO.setLastName( "xyz" );
        DateOfBirth dob = new DateOfBirth( 2, 3, 1998 );
        createNewCustomerRequestDTO.setDateOfBirth( dob );
        createNewCustomerRequestDTO.setEmail( emailId );
        createNewCustomerRequestDTO.setCellPhone( "9056482124" );
        createNewCustomerRequestDTO.setGender( "M" );
        createNewCustomerRequestDTO.setNationality( "Canadian" );
        createNewCustomerRequestDTO.setPhone( "777-444-8888" );
        createNewCustomerRequestDTO.setIp( "192.0.126.111" );

        // convert request object in to json object
        ObjectMapper objectMapper = new ObjectMapper();
        String jsonString = null;
        try {

            jsonString = objectMapper.writeValueAsString(createNewCustomerRequestDTO);
        }
        catch (JsonMappingException e) {
            e.printStackTrace();
        }
        catch (JsonGenerationException e) {
            e.printStackTrace();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println( jsonString );

        // build the request
        HttpEntity< String > entity = new HttpEntity< String >( jsonString, headers );

        // send POST request
        ResponseEntity< CreateNewCustomerResponseDTO > responseEntity = restTemplate.postForEntity( url, entity, CreateNewCustomerResponseDTO.class );

        // check if user is successfully created
        if( responseEntity.getStatusCode() == HttpStatus.CREATED ){

            // get the response
            CreateNewCustomerResponseDTO response = responseEntity.getBody();

            // create new record for the customer in local database and set it's attributes values
            Customer newCustomer = new Customer();
            newCustomer.setPaysafeId( response.getId() );
            newCustomer.setEmail( response.getEmail() );

            customerRepository.save( newCustomer );
            return newCustomer;
        }
        else {

            System.out.println( "failed user creation" );

            Customer c = null;
            return c;
        }

    }


    public MakePaymentResponseDTO makePayment( MakePaymentRequestDTO makePaymentRequestDTO ){

        System.out.println( "In make payment" );

        // create an url for the payemnt api
        String url = baseurl + "/payments";

        // create new http header and set content type to application/json
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));

        // set basic authorization with api key and its value
        headers.setBasicAuth( APIKeyId, APIKeyPassword );

        makePaymentRequestDTO.setCustomerIp( "10.10.12.64" );
        makePaymentRequestDTO.setMerchantRefNum( String.valueOf( rd.nextInt() ) );

        // convert request object into json object
        ObjectMapper objectMapper = new ObjectMapper();
        String jsonString = null;
        try {

            jsonString = objectMapper.writeValueAsString( makePaymentRequestDTO );
        }
        catch (JsonMappingException e) {
            e.printStackTrace();
        }
        catch (JsonGenerationException e) {
            e.printStackTrace();
        }
        catch (IOException e) {
            e.printStackTrace();
        }

        // build the request
        HttpEntity< String > entity = new HttpEntity<String>( jsonString, headers );

        // send POST request
        ResponseEntity<MakePaymentResponseDTO> responseEntity = restTemplate.postForEntity( url, entity, MakePaymentResponseDTO.class );

        if( responseEntity.getStatusCode() != HttpStatus.CREATED ){

            // throw an exception
        }

        return responseEntity.getBody();
    }
}
