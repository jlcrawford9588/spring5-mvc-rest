package guru.springfamework.api.v1.mapper;

import guru.springfamework.api.v1.model.CustomerDTO;
import guru.springfamework.domain.Customer;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class CustomerMapperTest {

    public static final String TEST_FIRST_NAME = "Joe";
    public static final String TEST_LAST_NAME = "Rogan";

    CustomerMapper customerMapper = CustomerMapper.INSTANCE;

    @Test
    public void toCustomerDTO() {
        //given
        Customer customer = new Customer();
        customer.setFirstName(TEST_FIRST_NAME);
        customer.setLastName(TEST_LAST_NAME);

        //when
        CustomerDTO customerDTO = customerMapper.toCustomerDTO(customer);

        //then
        assertEquals(TEST_FIRST_NAME, customerDTO.getFirstName());
        assertEquals(TEST_LAST_NAME, customerDTO.getLastName());
    }

    @Test
    public void toCustomer() {
        //given
        CustomerDTO customerDTO = new CustomerDTO();
        customerDTO.setFirstName(TEST_FIRST_NAME);
        customerDTO.setLastName(TEST_LAST_NAME);

        //when
        Customer customer = customerMapper.toCustomer(customerDTO);

        //then
        assertEquals(TEST_FIRST_NAME, customer.getFirstName());
        assertEquals(TEST_LAST_NAME, customer.getLastName());
    }
}