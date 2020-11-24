package pl.microservices.carservice.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
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
import pl.microservices.carservice.exception.ResourceNotFoundException;
import pl.microservices.carservice.model.Car;
import pl.microservices.carservice.service.CarService;
import pl.microservices.carservice.web.webclient.client.RentalClient;

import java.math.BigDecimal;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = CarController.class, useDefaultFilters = false)
@ComponentScan(basePackages = "pl.microservices.carservice.web")
@AutoConfigureMockMvc
class CarControllerIntegrationTest {
    private final long CAR_ID = 1;
    private final int PAGE = 1;
    private final int PAGE_SIZE = 1;

    private final String BASE_URL = "/api/v1/cars";
    private final String CAR_URL = BASE_URL + "/" + CAR_ID;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private CarService carService;

    @MockBean
    private RentalClient rentalClient;

    @DisplayName("Given cars are being requested")
    @Nested
    @WithMockUser(username = "USERNAME")
    class GetAll {
        private List<Car> cars;

        @BeforeEach
        public void setup() {
            cars = List.of(new Car(), new Car());

            when(carService.findAll())
                    .thenReturn(cars);
        }

        @DisplayName("Then returns a collection of all cars")
        @Test
        public void shouldReturnAllCars() throws Exception {
            mockMvc.perform(get(BASE_URL))
                    .andDo(print())
                    .andExpect(content().json(objectMapper.writeValueAsString(cars)));
        }

        @DisplayName("Then return HTTP status OK")
        @Test
        public void shouldReturnHttpStatusOk() throws Exception {
            mockMvc.perform(get(BASE_URL))
                    .andDo(print())
                    .andExpect(status().isOk());
        }

        @DisplayName("When paging is defined")
        @Nested
        @WithMockUser(username = "USERNAME")
        class WithPaging {
            private MultiValueMap<String, String> params;

            @Mock
            private Page<Car> carPage;

            @BeforeEach
            public void setup() {
                params = new LinkedMultiValueMap<>();
                params.add("page", String.valueOf(PAGE));
                params.add("size", String.valueOf(PAGE_SIZE));

                when(carService.findAll(PAGE, PAGE_SIZE))
                        .thenReturn(carPage);

                when(carPage.getContent())
                        .thenReturn(cars);
            }

            @DisplayName("Then returns a collection of cars")
            @Test
            public void shouldReturnCars() throws Exception {
                mockMvc.perform(get(BASE_URL).params(params))
                        .andDo(print())
                        .andExpect(content().json(objectMapper.writeValueAsString(cars)));
            }

            @DisplayName("Then returns HTTP status OK")
            @Test
            public void shouldReturnHttpStatusOk() throws Exception {
                mockMvc.perform(get(BASE_URL).params(params))
                        .andDo(print())
                        .andExpect(status().isOk());
            }
        }
    }

    @DisplayName("Given a car is being requested")
    @Nested
    @WithMockUser(username = "USERNAME")
    class GetOne {
        private Car car;

        @BeforeEach
        public void setup() {
            car = new Car();

            when(carService.findById(CAR_ID))
                    .thenReturn(car);
        }

        @DisplayName("Then returns a car")
        @Test
        public void shouldReturnCar() throws Exception {
            mockMvc.perform(get(CAR_URL))
                    .andDo(print())
                    .andExpect(content().json(objectMapper.writeValueAsString(car)));
        }

        @DisplayName("Then returns HTTP status OK")
        @Test
        public void shouldReturnHttpStatusOk() throws Exception {
            mockMvc.perform(get(CAR_URL))
                    .andDo(print())
                    .andExpect(status().isOk());
        }

        @DisplayName("When car isn't found")
        @Nested
        @WithMockUser(username = "USERNAME")
        class CarNotFound {

            @DisplayName("Then return HTTP status Not Found")
            @Test
            public void shouldReturnHttpStatusNotFound() throws Exception {
                when(carService.findById(CAR_ID))
                        .thenThrow(ResourceNotFoundException.class);

                mockMvc.perform(get(CAR_URL))
                        .andDo(print())
                        .andExpect(status().isNotFound());
            }
        }
    }

    @DisplayName("Given a car is being saved")
    @Nested
    @WithMockUser(username = "USERNAME")
    class SavingCar {
        private Car car;
        private Car returnedCar;

        @BeforeEach
        public void setup() {
            car = new Car();
            returnedCar = new Car();
            returnedCar.setId(CAR_ID);

            when(carService.save(any()))
                    .thenReturn(returnedCar);
        }

        @DisplayName("Then returns resource id")
        @Test
        public void shouldReturnResourceId() throws Exception {
            mockMvc.perform(post(BASE_URL)
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(car)))
                    .andDo(print())
                    .andExpect(content().json(objectMapper.writeValueAsString(returnedCar.getId())));
        }

        @DisplayName("Then returns HTTP status Created")
        @Test
        public void shouldReturnHttpStatusCreated() throws Exception {
            mockMvc.perform(post(BASE_URL)
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(car)))
                    .andDo(print())
                    .andExpect(status().isCreated());
        }
    }

    @DisplayName("Given a car is being deleted")
    @Nested
    @WithMockUser(username = "USERNAME")
    class DeleteCar {

        @DisplayName("Then returns HTTP status No Content")
        @Test
        public void shouldReturnHttpStatusNoContent() throws Exception {
            when(rentalClient.isCarFreeInFuture(CAR_ID))
                    .thenReturn(true);

            mockMvc.perform(delete(CAR_URL)
                        .with(csrf()))
                    .andDo(print())
                    .andExpect(status().isNoContent());
        }

        @DisplayName("When car is already booked in the future")
        @Nested
        @WithMockUser(username = "USERNAME")
        class BookedCar {

            @DisplayName("Then returns HTTP status Bad Request")
            @Test
            public void shouldReturnHttpStatusBadRequest() throws Exception {
                when(rentalClient.isCarFreeInFuture(CAR_ID))
                        .thenReturn(false);

                mockMvc.perform(delete(CAR_URL)
                            .with(csrf()))
                        .andDo(print())
                        .andExpect(status().isBadRequest());
            }

        }
    }

    @DisplayName("Given a car is being updated")
    @Nested
    @WithMockUser(username = "USERNAME")
    class UpdateCar {

        @DisplayName("Then returns HTTP status No Content")
        @Test
        public void shouldReturnHttpStatusNoContent() throws Exception {
            Car car = new Car();
            car.setId(CAR_ID);

            mockMvc.perform(put(BASE_URL)
                                .with(csrf())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(car)))
                    .andDo(print())
                    .andExpect(status().isNoContent());
        }

        @DisplayName("When request lacks car id")
        @Nested
        @WithMockUser(username = "USERNAME")
        class NoCarId {

            @DisplayName("Then returns HTTP status Invalid Request")
            @Test
            public void shouldReturnHttpStatusInvalidRequest() throws Exception {
                Car car = new Car();

                mockMvc.perform(put(BASE_URL)
                                    .with(csrf())
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .content(objectMapper.writeValueAsString(car)))
                        .andDo(print())
                        .andExpect(status().isBadRequest());
            }
        }
    }

    @DisplayName("Given an invalid car is sent")
    @Nested
    @WithMockUser(username = "USERNAME")
    class InvalidCar {
        private Car invalidCar;

        @BeforeEach
        public void setup() {
            invalidCar = new Car();
            invalidCar.setPricePerDay(BigDecimal.ZERO);
        }

        @DisplayName("Then returns HTTP status Bad Request")
        @Test
        public void shouldReturnHttpStatusBadRequest() throws Exception {
            mockMvc.perform(post(BASE_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(invalidCar)))
                    .andDo(print())
                    .andExpect(status().isBadRequest());
        }
    }
}
