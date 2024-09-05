package dk.cphbusiness.persistence.daos;

import dk.cphbusiness.persistence.HibernateConfig;
import dk.cphbusiness.persistence.entities.Address;
import dk.cphbusiness.persistence.entities.Employee;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class IDAOTest {
    private static IDAO<Employee> dao;
    private static EntityManagerFactory emf;
    Employee e1, e2, e3, e4;
    Address a1, a2, a3;

    @BeforeAll
    static void beforeAll() {
        emf = HibernateConfig.getEntityManagerFactoryForTest();
        dao = new EmployeeDAO(emf);
    }

    @AfterAll
    static void afterAll() {
    }


    @BeforeEach
    void setUp() {
        try (EntityManager em = emf.createEntityManager()) {
            e1 = Employee.builder().name("Holger").email("holger@mail.dk").phone("+4567568790").salary(10000).build();
            e2 = Employee.builder().name("Holger").email("holger2@mail.dk").phone("+4568899009").salary(8000).build();
            e3 = Employee.builder().name("Henrietta").email("henri@mail.dk").phone("+4590918727").salary(13000).build();
            e4 = Employee.builder().name("Hassan").email("hassan@mail.dk").phone("+459903872341").salary(14000).build();
            a1 = Address.builder().street("Haraldsgade").streetNumber(44).zip(2100).city("Kbh N").build();
            a1 = Address.builder().street("Bopa Plads").streetNumber(55).zip(2200).city("Kbh Ø").build();
            a1 = Address.builder().street("Kongens Nytorv").streetNumber(66).zip(2300).city("City Nord").build();
            e1.setAddress(a1);
            e2.setAddress(a2);
            e3.setAddress(a2);
            em.getTransaction().begin();
            em.createQuery("DELETE FROM Employee").executeUpdate();
            em.createQuery("DELETE FROM Address ").executeUpdate();
            em.persist(e1);
            em.persist(e2);
            em.persist(e3);
        }

    }

    @AfterEach
    void tearDown() {
    }

    @Test
    @DisplayName("Test create Employee")
    void create() {
        Employee emp = Employee.builder().name("Inga").email("inga@mail.dk").phone("+4690212134").salary(15000).build();
        emp.setAddress(Address.builder().street("Galvej").streetNumber(11).zip(6660).city("Gammelby").build());
        Employee created = dao.create(emp);
        boolean expected = created.getId() != null;
        assertTrue(expected);
    }

    @Test
    @DisplayName("Test get Employee by ID")
    void getById() {
        Employee actual = dao.getById(e1.getId());
        Employee expected = e1;
        assertEquals(actual, expected);
    }

    @Test
    @DisplayName("Test Update Employee both name and address")
    void update() {
        Address newAddress = Address.builder()
                .street("Rolighedsvej 33")
                .streetNumber(33)
                .zip(3999)
                .city("Helsingør")
                .build();
        e1.setAddress(newAddress);
        dao.update(e1);
        int expected = 33;
        int actual = dao.getById(e1.getId()).getAddress().getStreetNumber();
        assertEquals(expected, actual);
    }

    @Test
    @DisplayName("Test delete employee by id")
    void delete() {
        int numberOf = dao.getAll().size();
        dao.delete(e3);
        int expected = numberOf-1;
        int actual = dao.getAll().size();
        assertEquals(expected,actual);
    }

    @Test
    @DisplayName("Test getting all employees")
    void getAllEmployees() {
        int expected = 3;
        int actual = dao.getAll().size();
        assertEquals(expected, actual);
    }
}