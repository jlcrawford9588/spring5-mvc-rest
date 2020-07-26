package guru.springfamework.controllers.v1;

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
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

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
        testCustomerDTO2.setFirstName(TEST_LAST_NAME_2);
        testCustomerDTO2.setLastName(TEST_LAST_NAME_2);
        when(customerService.getAllCustomers()).thenReturn(Arrays.asList(testCustomerDTO, testCustomerDTO2));

        //when then
        mockMvc.perform(
                MockMvcRequestBuilders.get("/api/v1/customers").contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.customers", Matchers.hasSize(2)));
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
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.firstName", Matchers.equalTo(TEST_FIRST_NAME_1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.lastName", Matchers.equalTo(TEST_LAST_NAME_1)));
    }
}