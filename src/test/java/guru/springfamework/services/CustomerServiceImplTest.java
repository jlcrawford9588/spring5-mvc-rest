package guru.springfamework.services;

import guru.springfamework.api.v1.mapper.CustomerMapper;
import guru.springfamework.api.v1.model.CustomerDTO;
import guru.springfamework.domain.Customer;
import guru.springfamework.repositories.CustomerRepository;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class CustomerServiceImplTest {

    private static final Long TEST_ID_1 = 1L;
    private static final String TEST_FIRST_NAME_1 = "George";
    private static final String TEST_LAST_NAME_1 = "Lopez";

    private static final Long TEST_ID_2 = 2L;
    private static final String TEST_FIRST_NAME_2 = "Johnny";
    private static final String TEST_LAST_NAME_2 = "Depp";

    CustomerService customerService;

    @Mock
    CustomerRepository customerRepository;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);

        customerService = new CustomerServiceImpl(customerRepository, CustomerMapper.INSTANCE);
    }

    @Test
    public void getAllCustomers() {
        //given
        Customer customer1 = new Customer();
        customer1.setId(TEST_ID_1);
        customer1.setFirstName(TEST_FIRST_NAME_1);
        customer1.setLastName(TEST_LAST_NAME_1);

        Customer customer2 = new Customer();
        customer2.setId(TEST_ID_2);
        customer2.setFirstName(TEST_FIRST_NAME_2);
        customer2.setLastName(TEST_LAST_NAME_2);

        when(customerRepository.findAll()).thenReturn(Arrays.asList(customer1, customer2));

        //when
        List<CustomerDTO> customerDTOList = customerService.getAllCustomers();

        //then
        assertEquals(2, customerDTOList.size());
    }

    @Test
    public void getCustomerById() {
        //given
        Customer customer1 = new Customer();
        customer1.setId(TEST_ID_1);
        customer1.setFirstName(TEST_FIRST_NAME_1);
        customer1.setLastName(TEST_LAST_NAME_1);

        when(customerRepository.findById(TEST_ID_1)).thenReturn(Optional.of(customer1));

        //when
        CustomerDTO customerDTO = customerService.getCustomerById(TEST_ID_1);

        //then
        assertEquals(TEST_FIRST_NAME_1, customerDTO.getFirstName());
        assertEquals(TEST_LAST_NAME_1, customerDTO.getLastName());
    }

    @Test
    public void createNewCustomer() {
        //given
        CustomerDTO customerDTO = createCustomerDto1();

        Customer savedCustomer = new Customer();
        savedCustomer.setFirstName(customerDTO.getFirstName());
        savedCustomer.setLastName(customerDTO.getLastName());
        savedCustomer.setId(22L);

        when(customerRepository.save(any(Customer.class))).thenReturn(savedCustomer);

        //when
        CustomerDTO savedDto = customerService.createNewCustomer(customerDTO);

        //then
        verify(customerRepository, times(1)).save(any());
        assertEquals(TEST_FIRST_NAME_1, savedDto.getFirstName());
        assertEquals(TEST_LAST_NAME_1, savedDto.getLastName());
    }

    @Test
    public void updateCustomer() {
        //given
        CustomerDTO customerDTO = createCustomerDto1();

        Customer savedCustomer = new Customer();
        savedCustomer.setFirstName(customerDTO.getFirstName());
        savedCustomer.setLastName(customerDTO.getLastName());
        savedCustomer.setId(1L);

        when(customerRepository.save(any(Customer.class))).thenReturn(savedCustomer);

        //when
        CustomerDTO savedDto = customerService.updateCustomer(savedCustomer.getId(), customerDTO);

        //then
        assertEquals(customerDTO.getFirstName(), savedDto.getFirstName());
        assertEquals("/api/v1/customers/1", savedDto.getCustomerUrl());
        verify(customerRepository, times(1)).save(any(Customer.class));
    }

    private CustomerDTO createCustomerDto1() {
        CustomerDTO customerDTO = new CustomerDTO();
        customerDTO.setFirstName(TEST_FIRST_NAME_1);
        customerDTO.setLastName(TEST_LAST_NAME_1);
        return customerDTO;
    }
}