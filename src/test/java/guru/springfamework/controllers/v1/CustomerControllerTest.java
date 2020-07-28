package guru.springfamework.controllers.v1;

import com.fasterxml.jackson.databind.ObjectMapper;
import guru.springfamework.api.v1.model.CustomerDTO;
import guru.springfamework.services.CustomerService;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;

import static org.hamcrest.Matchers.equalTo;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class CustomerControllerTest {

    private static final String TEST_FIRST_NAME_1 = "John";
    private static final String TEST_LAST_NAME_1 = "Wick";

    private static final String TEST_FIRST_NAME_2 = "George";
    private static final String TEST_LAST_NAME_2 = "Clooney";


    @Mock
    CustomerService customerService;

    @InjectMocks
    CustomerController customerController;

    MockMvc mockMvc;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);

        mockMvc = MockMvcBuilders.standaloneSetup(customerController).build();
    }

    @Test
    public void getAllCustomers() throws Exception {
        //given
        CustomerDTO testCustomerDTO = new CustomerDTO();
        testCustomerDTO.setFirstName(TEST_FIRST_NAME_1);
        testCustomerDTO.setLastName(TEST_LAST_NAME_1);

        CustomerDTO testCustomerDTO2 = new CustomerDTO();
        testCustomerDTO2.setFirstName(TEST_FIRST_NAME_2);
        testCustomerDTO2.setLastName(TEST_LAST_NAME_2);
        when(customerService.getAllCustomers()).thenReturn(Arrays.asList(testCustomerDTO, testCustomerDTO2));

        //when then
        mockMvc.perform(
                MockMvcRequestBuilders.get("/api/v1/customers").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.customers", Matchers.hasSize(2)));
    }

    @Test
    public void getCustomerById() throws Exception {
        //given
        CustomerDTO customerDTO = new CustomerDTO();
        customerDTO.setFirstName(TEST_FIRST_NAME_1);
        customerDTO.setLastName(TEST_LAST_NAME_1);

        when(customerService.getCustomerById(anyLong())).thenReturn(customerDTO);

        //when then
        mockMvc.perform(
                MockMvcRequestBuilders.get("/api/v1/customers/22").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName", equalTo(TEST_FIRST_NAME_1)))
                .andExpect(jsonPath("$.lastName", equalTo(TEST_LAST_NAME_1)));
    }

    @Test
    public void createCustomer() throws Exception {
        //given
        CustomerDTO customerDto = new CustomerDTO();
        customerDto.setFirstName(TEST_FIRST_NAME_1);
        customerDto.setLastName(TEST_LAST_NAME_1);

        CustomerDTO returnDto = new CustomerDTO();
        returnDto.setFirstName(customerDto.getFirstName());
        returnDto.setLastName(customerDto.getLastName());
        returnDto.setCustomerUrl("/api/v1/customers/1");

        when(customerService.createNewCustomer(any(CustomerDTO.class))).thenReturn(returnDto);

        //when then
        mockMvc.perform(post("/api/v1/customers/")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(customerDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.firstName", equalTo(TEST_FIRST_NAME_1)))
                .andExpect(jsonPath("$.customer_url", equalTo("/api/v1/customers/1")));
    }

    @Test
    public void updateCustomer() throws Exception {
        //given
        CustomerDTO customerDto = new CustomerDTO();
        customerDto.setFirstName(TEST_FIRST_NAME_1);
        customerDto.setLastName(TEST_LAST_NAME_1);

        CustomerDTO returnDto = new CustomerDTO();
        returnDto.setFirstName(customerDto.getFirstName());
        returnDto.setLastName(customerDto.getLastName());
        returnDto.setCustomerUrl("/api/v1/customers/1");

        when(customerService.updateCustomer(1L, customerDto)).thenReturn(returnDto);

        //when then
        mockMvc.perform(put("/api/v1/customers/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(customerDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName", equalTo(TEST_FIRST_NAME_1)))
                .andExpect(jsonPath("$.customer_url", equalTo("/api/v1/customers/1")));
    }


    private String asJsonString(Object object) {
        try {
            return new ObjectMapper().writeValueAsString(object);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }
}