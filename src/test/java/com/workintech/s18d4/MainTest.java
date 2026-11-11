package com.workintech.s18d4;

import com.workintech.s18d4.entity.Account;
import com.workintech.s18d4.entity.Address;
import com.workintech.s18d4.entity.Customer;
import com.workintech.s18d4.repository.AccountRepository;
import com.workintech.s18d4.repository.AddressRepository;
import com.workintech.s18d4.repository.CustomerRepository;
import com.workintech.s18d4.service.AccountServiceImpl;
import com.workintech.s18d4.service.CustomerServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@DataJpaTest
@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
@ExtendWith(ResultAnalyzer.class)
class MainTest {

    private Account sampleAccountForAccountEntity;
    private Customer sampleCustomerForCustomerEntity;

    private Address sampleAddressForAddressEntity;
    private Customer sampleCustomerForCustomerEntityTest;
    private Account sampleAccountForCustomerEntityTest;

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private AddressRepository addressRepository;

    private Address sampleAddressForAddressRepositoryTest;
    private Account sampleAccountForAccountRepositoryTest;

    @Autowired
    private CustomerRepository customerRepository;

    private Customer sampleCustomerForCustomerRepositoryTest;
    @Mock
    private AccountRepository mockAccountRepository;


    private AccountServiceImpl accountService;

    private Account sampleAccountForAccountServiceTest;

    @Mock
    private CustomerRepository mockCustomerRepository;


    private CustomerServiceImpl customerService;

    private Customer sampleCustomerForCustomerServiceTest;

    @BeforeEach
    void setUp() {
        sampleAccountForAccountEntity = new Account();
        sampleAccountForAccountEntity.setId(1L);
        sampleAccountForAccountEntity.setAccountName("Savings");
        sampleAccountForAccountEntity.setMoneyAmount(1500.0);

        sampleCustomerForCustomerEntity = new Customer();
        sampleCustomerForCustomerEntity.setId(1L);
        sampleCustomerForCustomerEntity.setFirstName("John");
        sampleCustomerForCustomerEntity.setLastName("Doe");

        sampleAccountForAccountEntity.setCustomer(sampleCustomerForCustomerEntity);


        sampleAddressForAddressEntity = new Address();
        sampleAddressForAddressEntity.setId(1L);
        sampleAddressForAddressEntity.setStreet("Main Street");
        sampleAddressForAddressEntity.setNo("100");
        sampleAddressForAddressEntity.setCity("Sample City");
        sampleAddressForAddressEntity.setCountry("Sample Country");
        sampleAddressForAddressEntity.setDescription("Near the big landmark");

        sampleCustomerForCustomerEntityTest = new Customer();
        sampleCustomerForCustomerEntityTest.setId(2L);
        sampleCustomerForCustomerEntityTest.setFirstName("Jane");
        sampleCustomerForCustomerEntityTest.setLastName("Doe");
        sampleCustomerForCustomerEntityTest.setEmail("jane.doe@example.com");
        sampleCustomerForCustomerEntityTest.setSalary(3000.0);

        sampleAccountForCustomerEntityTest = new Account();
        sampleAccountForCustomerEntityTest.setId(2L);
        sampleAccountForCustomerEntityTest.setAccountName("Checking");
        sampleAccountForCustomerEntityTest.setMoneyAmount(2500.0);

        sampleCustomerForCustomerEntityTest.setAccounts(List.of(sampleAccountForCustomerEntityTest));
        sampleAccountForCustomerEntityTest.setCustomer(sampleCustomerForCustomerEntityTest);


        Customer customer = new Customer();
        customer.setFirstName("John");
        customer.setLastName("Doe");
        customer.setEmail("john.doe@example.com");
        customer = entityManager.persistFlushFind(customer);


        sampleAccountForAccountRepositoryTest = new Account();
        sampleAccountForAccountRepositoryTest.setAccountName("Savings Account");
        sampleAccountForAccountRepositoryTest.setMoneyAmount(1000.00);
        sampleAccountForAccountRepositoryTest.setCustomer(customer);
        entityManager.persistFlushFind(sampleAccountForAccountRepositoryTest);

        sampleAddressForAddressRepositoryTest = new Address();
        sampleAddressForAddressRepositoryTest.setStreet("Main Street");
        sampleAddressForAddressRepositoryTest.setNo("42");
        sampleAddressForAddressRepositoryTest.setCity("Sample City");
        sampleAddressForAddressRepositoryTest.setCountry("Sample Country");
        sampleAddressForAddressRepositoryTest.setDescription("Near the park");
        entityManager.persistFlushFind(sampleAddressForAddressRepositoryTest);

        Address address = new Address();
        address.setStreet("Main Street");
        address.setNo("42");
        address.setCity("Sample City");
        address.setCountry("Sample Country");
        entityManager.persist(address);

        sampleCustomerForCustomerRepositoryTest = new Customer();
        sampleCustomerForCustomerRepositoryTest.setFirstName("John");
        sampleCustomerForCustomerRepositoryTest.setLastName("Doe");
        sampleCustomerForCustomerRepositoryTest.setEmail("john.doe@example.com");
        sampleCustomerForCustomerRepositoryTest.setSalary(50000.0);
        sampleCustomerForCustomerRepositoryTest.setAddress(address);
        entityManager.persist(sampleCustomerForCustomerRepositoryTest);

        MockitoAnnotations.openMocks(this);
        sampleAccountForAccountServiceTest = new Account();
        sampleAccountForAccountServiceTest.setId(1L);
        sampleAccountForAccountServiceTest.setAccountName("Savings Account");
        sampleAccountForAccountServiceTest.setMoneyAmount(1000.00);
        accountService = new AccountServiceImpl(mockAccountRepository);

        sampleCustomerForCustomerServiceTest = new Customer();
        sampleCustomerForCustomerServiceTest.setId(1L);
        sampleCustomerForCustomerServiceTest.setFirstName("John");
        sampleCustomerForCustomerServiceTest.setLastName("Doe");
        sampleCustomerForCustomerServiceTest.setEmail("john.doe@example.com");

        customerService = new CustomerServiceImpl(mockCustomerRepository);
    }

    @Test
    @DisplayName("Test Account Entity Getters and Setters")
    void testAccountProperties() {
        assertEquals(1L, sampleAccountForAccountEntity.getId());
        assertEquals("Savings", sampleAccountForAccountEntity.getAccountName());
        assertEquals(1500.0, sampleAccountForAccountEntity.getMoneyAmount(), 0.001);
        assertEquals(sampleCustomerForCustomerEntity, sampleAccountForAccountEntity.getCustomer());
        assertEquals("John", sampleAccountForAccountEntity.getCustomer().getFirstName());
    }

    @Test
    @DisplayName("Test Address Entity Getters and Setters")
    void testAddressProperties() {
        assertEquals(1L, sampleAddressForAddressEntity.getId());
        assertEquals("Main Street", sampleAddressForAddressEntity.getStreet());
        assertEquals("100", sampleAddressForAddressEntity.getNo());
        assertEquals("Sample City", sampleAddressForAddressEntity.getCity());
        assertEquals("Sample Country", sampleAddressForAddressEntity.getCountry());
        assertEquals("Near the big landmark", sampleAddressForAddressEntity.getDescription());

        // Testing nullability for description
        sampleAddressForAddressEntity.setDescription(null);
        assertNull(sampleAddressForAddressEntity.getDescription());
    }

    @Test
    @DisplayName("Test Customer Association")
    void testCustomerAssociation() {
        Customer customer = new Customer(); // Assuming you have a default constructor
        customer.setEmail("John Doe"); // Assuming Customer has this setter
        sampleAddressForAddressEntity.setCustomer(customer);

        assertEquals("John Doe", sampleAddressForAddressEntity.getCustomer().getEmail());
    }

    @Test
    @DisplayName("Test Customer Entity Getters and Setters")
    void testCustomerProperties() {
        assertEquals(2L, sampleCustomerForCustomerEntityTest.getId());
        assertEquals("Jane", sampleCustomerForCustomerEntityTest.getFirstName());
        assertEquals("Doe", sampleCustomerForCustomerEntityTest.getLastName());
        assertEquals("jane.doe@example.com", sampleCustomerForCustomerEntityTest.getEmail());
        assertEquals(3000.0, sampleCustomerForCustomerEntityTest.getSalary(), 0.001);
        assertTrue(sampleCustomerForCustomerEntityTest.getAccounts().contains(sampleAccountForCustomerEntityTest));
        assertEquals(sampleCustomerForCustomerEntityTest, sampleCustomerForCustomerEntityTest.getAccounts().get(0).getCustomer());
    }

    @Test
    @DisplayName("AccountRepositoryTest::save")
    void testSaveAndFindByIdAccount() {

        Optional<Account> foundAccount = accountRepository.findById(sampleAccountForAccountRepositoryTest.getId());
        assertTrue(foundAccount.isPresent());
        assertEquals(sampleAccountForAccountRepositoryTest.getAccountName(), foundAccount.get().getAccountName());
    }

    @Test
    @DisplayName("AccountRepositoryTest::update")
    void testUpdateAccount() {

        sampleAccountForAccountRepositoryTest.setMoneyAmount(2000.00);
        accountRepository.save(sampleAccountForAccountRepositoryTest);
        Optional<Account> updatedAccount = accountRepository.findById(sampleAccountForAccountRepositoryTest.getId());
        assertTrue(updatedAccount.isPresent());
        assertEquals(2000.00, updatedAccount.get().getMoneyAmount());
    }

    @Test
    @DisplayName("AccountRepositoryTest::delete")
    void testDeleteAccount() {

        accountRepository.delete(sampleAccountForAccountRepositoryTest);
        Optional<Account> deletedAccount = accountRepository.findById(sampleAccountForAccountRepositoryTest.getId());
        assertTrue(deletedAccount.isEmpty());
    }

    @Test
    @DisplayName("AddressRepositoryTest::save")
    void testSaveAndFindByIdAddress() {

        Optional<Address> foundAddress = addressRepository.findById(sampleAddressForAddressRepositoryTest.getId());
        assertTrue(foundAddress.isPresent());
        assertEquals("Main Street", foundAddress.get().getStreet());
    }

    @Test
    @DisplayName("AddressRepositoryTest::update")
    void testUpdateAddress() {

        sampleAddressForAddressRepositoryTest.setCity("Updated City");
        addressRepository.save(sampleAddressForAddressRepositoryTest);
        Optional<Address> updatedAddress = addressRepository.findById(sampleAddressForAddressRepositoryTest.getId());
        assertTrue(updatedAddress.isPresent());
        assertEquals("Updated City", updatedAddress.get().getCity());
    }

    @Test
    @DisplayName("AddressRepositoryTest::delete")
    void testDeleteAddress() {

        addressRepository.delete(sampleAddressForAddressRepositoryTest);
        Optional<Address> deletedAddress = addressRepository.findById(sampleAddressForAddressRepositoryTest.getId());
        assertTrue(deletedAddress.isEmpty());
    }

    @Test
    @DisplayName("CustomerRepositoryTest::save")
    void testSaveAndFindByIdCustomer() {

        Optional<Customer> foundCustomer = customerRepository.findById(sampleCustomerForCustomerRepositoryTest.getId());
        assertTrue(foundCustomer.isPresent());
        assertEquals("John", foundCustomer.get().getFirstName());
    }

    @Test
    @DisplayName("CustomerRepositoryTest::update")
    void testUpdateCustomer() {

        sampleCustomerForCustomerRepositoryTest.setEmail("updated.email@example.com");
        customerRepository.save(sampleCustomerForCustomerRepositoryTest);
        Optional<Customer> updatedCustomer = customerRepository.findById(sampleCustomerForCustomerRepositoryTest.getId());
        assertTrue(updatedCustomer.isPresent());
        assertEquals("updated.email@example.com", updatedCustomer.get().getEmail());
    }

    @Test
    @DisplayName("CustomerRepositoryTest::delete")
    void testDeleteCustomer() {

        customerRepository.delete(sampleCustomerForCustomerRepositoryTest);
        Optional<Customer> deletedCustomer = customerRepository.findById(sampleCustomerForCustomerRepositoryTest.getId());
        assertFalse(deletedCustomer.isPresent());
    }

    @Test
    @DisplayName("AccountService::findAll")
    void testFindAllAccount_AccountService() {
        when(mockAccountRepository.findAll()).thenReturn(Arrays.asList(sampleAccountForAccountServiceTest));
        List<Account> result = accountService.findAll();
        assertEquals(1, result.size());
        assertEquals(sampleAccountForAccountServiceTest, result.get(0));
    }

    @Test
    @DisplayName("AccountService::find")
    void testFindAccount_AccountService() {
        when(mockAccountRepository.findById(1L)).thenReturn(Optional.of(sampleAccountForAccountServiceTest));
        Account result = accountService.find(1L);
        assertEquals(sampleAccountForAccountServiceTest, result);
    }

    @Test
    @DisplayName("AccountService::save")
    void testSaveAccount_AccountService() {
        when(mockAccountRepository.save(any(Account.class))).thenReturn(sampleAccountForAccountServiceTest);
        Account savedAccount = accountService.save(sampleAccountForAccountServiceTest);
        assertEquals(sampleAccountForAccountServiceTest, savedAccount);
    }

    @Test
    @DisplayName("AccountService::delete")
    void testDeleteAccount_AccountService() {
        when(mockAccountRepository.findById(1L)).thenReturn(Optional.of(sampleAccountForAccountServiceTest));
        doNothing().when(mockAccountRepository).delete(sampleAccountForAccountServiceTest);
        accountService.delete(1L);
        verify(mockAccountRepository, times(1)).delete(sampleAccountForAccountServiceTest);
    }

    @Test
    @DisplayName("AccountService::delete notfound")
    void testDeleteNotFoundAccount_AccountService() {
        when(mockAccountRepository.findById(anyLong())).thenReturn(Optional.empty());
        assertNull(accountService.delete(1L));
    }


    @Test
    @DisplayName("CustomerService::findAll")
    void testFindAllCustomer() {
        when(mockCustomerRepository.findAll()).thenReturn(Arrays.asList(sampleCustomerForCustomerServiceTest));
        List<Customer> result = customerService.findAll();
        assertEquals(1, result.size());
        assertEquals(sampleCustomerForCustomerServiceTest, result.get(0));
    }

    @Test
    @DisplayName("CustomerService::find")
    void testFindCustomer() {
        when(mockCustomerRepository.findById(1L)).thenReturn(Optional.of(sampleCustomerForCustomerServiceTest));
        Customer result = customerService.find(1L);
        assertEquals(sampleCustomerForCustomerServiceTest, result);
    }

    @Test
    @DisplayName("CustomerService::save")
    void testSaveCustomer() {
        when(mockCustomerRepository.save(any(Customer.class))).thenReturn(sampleCustomerForCustomerServiceTest);
        Customer savedCustomer = customerService.save(sampleCustomerForCustomerServiceTest);
        assertEquals(sampleCustomerForCustomerServiceTest, savedCustomer);
    }

    @Test
    @DisplayName("CustomerService::delete")
    void testDeleteCustomerService() {
        when(mockCustomerRepository.findById(1L)).thenReturn(Optional.of(sampleCustomerForCustomerServiceTest));
        doNothing().when(mockCustomerRepository).delete(sampleCustomerForCustomerServiceTest);
        Customer deletedCustomer = customerService.delete(1L);
        assertEquals(sampleCustomerForCustomerServiceTest, deletedCustomer);
    }

    @Test
    @DisplayName("CustomerService::delete - Customer not found")
    void testDeleteNotFoundCustomer() {
        when(mockCustomerRepository.findById(anyLong())).thenReturn(Optional.empty());
        assertNull(customerService.delete(1L));
    }
}
