package pl.microservices.rentalservice.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.*;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import pl.microservices.rentalservice.model.Rental;
import pl.microservices.rentalservice.service.RentalService;

import java.time.LocalDate;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = RentalController.class, useDefaultFilters = false)
@ComponentScan(basePackages = "pl.microservices.rentalservice.web")
@AutoConfigureMockMvc
class RentalControllerIntegrationTest {
    private static final int PAGE = 1;
    private static final int PAGE_SIZE = 1;

    private static Rental rental;

    private static final String USERNAME = "USERNAME";

    private static final long CAR_ID = 1L;
    private final long RENTAL_ID = 1L;

    private static List<Rental> rentals;

    private final String BASE_URL = "/api/v1/rentals";
    private final String USER_URL = BASE_URL + "/users/" + USERNAME;
    private final String RENTAL_URL = BASE_URL + "/" + RENTAL_ID;
    private final String CAR_URL = BASE_URL + "/cars/" + CAR_ID + "/isFree";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private RentalService rentalService;

    @BeforeAll
    public static void setup() {
        rentals = List.of(new Rental(), new Rental());

        rental = new Rental();
        rental.setUsername(USERNAME);
        rental.setCarId(CAR_ID);
    }

    @DisplayName("Given all rentals are requested")
    @Nested
    class GetAll {

        @DisplayName("When paging is defined")
        @Nested
        @WithMockUser
        class WithPaging {
            private MultiValueMap<String, String> params;

            @Mock
            private Page<Rental> carPage;

            @BeforeEach
            public void setup() {
                params = new LinkedMultiValueMap<>();
                params.add("page", String.valueOf(PAGE));
                params.add("size", String.valueOf(PAGE_SIZE));

                when(rentalService.findAll(PAGE, PAGE_SIZE))
                        .thenReturn(carPage);

                when(carPage.getContent())
                        .thenReturn(rentals);
            }

            @DisplayName("Then returns a collection of rentals")
            @Test
            public void shouldReturnCollectionOfRentals() throws Exception {
                mockMvc.perform(get(BASE_URL).params(params))
                        .andExpect(content().json(objectMapper.writeValueAsString(rentals)));
            }

            @DisplayName("Then returns HTTP status OK")
            @Test
            public void shouldReturnHttpStatusOk() throws Exception {
                mockMvc.perform(get(BASE_URL).params(params))
                        .andExpect(status().isOk());
            }
        }
    }

    @DisplayName("Given rentals are requested by username")
    @Nested
    @WithMockUser
    class GetUsersRentals {

        @BeforeEach
        public void setup() {
            when(rentalService.findByUsername(USERNAME))
                    .thenReturn(rentals);
        }

        @DisplayName("Then returns a collection of rentals")
        @Test
        public void shouldReturnRentalCollection() throws Exception {
            mockMvc.perform(get(USER_URL))
                    .andExpect(content().json(objectMapper.writeValueAsString(rentals)));
        }

        @DisplayName("Then returns a HTTP status OK")
        @Test
        public void shouldReturnHttpStatusOk() throws Exception {
            mockMvc.perform(get(USER_URL))
                    .andExpect(content().json(objectMapper.writeValueAsString(rentals)));
        }
    }

    @DisplayName("Given a rental is being saved")
    @Nested
    @WithMockUser
    class SaveRental {
        private Rental persistedRental;

        @BeforeEach
        public void setup() {
            persistedRental = new Rental();
            persistedRental.setId(RENTAL_ID);

            when(rentalService.save(any(Rental.class)))
                    .thenReturn(persistedRental);
        }

        @DisplayName("Then return resource id")
        @Test
        public void shouldReturnResourceId() throws Exception {
            mockMvc.perform(post(BASE_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(rental)))
                    .andExpect(content().json(objectMapper.writeValueAsString(persistedRental.getId())));
        }

        @DisplayName("Then returns HTTP status Created")
        @Test
        public void shouldReturnHttpStatusCreated() throws Exception {
            mockMvc.perform(post(BASE_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(rental)))
                    .andExpect(status().isCreated());
        }
    }

    @DisplayName("Given a rental is being updated")
    @Nested
    @WithMockUser
    class UpdateRental {

        @DisplayName("Then returns HTTP status No Content")
        @Test
        public void shouldReturnHttpStatusNoContent() throws Exception {
            rental.setId(RENTAL_ID);

            when(rentalService.save(any(Rental.class)))
                    .thenReturn(rental);

            mockMvc.perform(put(BASE_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(rental)))
                    .andExpect(status().isNoContent());
        }

        @DisplayName("When id is not provided")
        @Nested
        @WithMockUser
        class NullId {

            @DisplayName("Then returns HTTP status Bad Request")
            @Test
            public void shouldReturnHttpStatusBadRequest() throws Exception {
                Rental rental = new Rental();
                rental.setUsername(USERNAME);
                rental.setCarId(CAR_ID);

                mockMvc.perform(put(BASE_URL)
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(rental)))
                        .andExpect(status().isBadRequest());
            }
        }
    }

    @DisplayName("Given a rental is being deleted")
    @Nested
    @WithMockUser
    class DeleteRental {

        @DisplayName("Then returns HTTP status No Content")
        @Test
        public void shouldReturnHttpStatusNoContent() throws Exception {
            mockMvc.perform(delete(RENTAL_URL)
                    .with(csrf()))
                    .andExpect(status().isNoContent());
        }
    }

    @DisplayName("Given a car is being checked for future rentals")
    @Nested
    @WithMockUser
    class RentalAvailability {

        @DisplayName("Then returns whether car is completely free in the future")
        @Test
        public void shouldReturnWhetherCarIsFree() throws Exception {
            when(rentalService.isCarFreeInFuture(CAR_ID))
                    .thenReturn(true);

            mockMvc.perform(get(CAR_URL))
                    .andExpect(content().string(Boolean.TRUE.toString()));
        }
    }

    @DisplayName("Given a car is being checked for rentals within a timeframe")
    @Nested
    @WithMockUser
    class RentalTimeframeAvailability {

        @DisplayName("Then returns whether car is free between dates")
        @Test
        public void shouldReturnWhetherCarIsFree() throws Exception {
            MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
            params.add("start", LocalDate.now().toString());
            params.add("end", LocalDate.now().toString());

            when(rentalService.isCarFree(eq(CAR_ID), any(LocalDate.class), any(LocalDate.class)))
                    .thenReturn(true);

            mockMvc.perform(get(CAR_URL)
                        .params(params))
                    .andExpect(content().string(Boolean.TRUE.toString()));
        }
    }
}
