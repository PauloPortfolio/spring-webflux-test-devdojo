package academy.devdojo.webflux.controller.CRUD_USER_INTEGR;

import academy.devdojo.webflux.GlobalTestConfig;
import academy.devdojo.webflux.entity.Anime;
import academy.devdojo.webflux.utils.RoleUsersHeaders;
import io.restassured.http.ContentType;
import io.restassured.module.webtestclient.RestAssuredWebTestClient;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.blockhound.BlockingOperationError;
import reactor.core.scheduler.Schedulers;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import static academy.devdojo.webflux.databuilder.AnimeCreatorBuilder.animeEmpty;
import static academy.devdojo.webflux.databuilder.AnimeCreatorBuilder.animeWithName;
import static org.hamcrest.Matchers.*;
import static org.springframework.http.HttpStatus.*;

public class Delete extends GlobalTestConfig {

    //WEB-TEST-CLIENT(non-blocking client) WITH MOCK-SERVER
    @Autowired
    WebTestClient testClient;

    @Autowired
    private WebTestClient webTestClient;

    private Anime anime_1, anime_2;

    @Before
    public void setUpLocal() {
        anime_1 = animeWithName().create();
        anime_2 = animeWithName().create();

        //REAL-SERVER(non-blocking client)  IN WEB-TEST-CLIENT:
        webTestClient = WebTestClient.bindToServer().baseUrl("http://localhost:8080/animes").build();
    }

    @After
    public void tearDownLocal() {
//        service.delete(anime.getId());
    }

    @Test
    public void delete() {
        RestAssuredWebTestClient
                .given()
                .webTestClient(webTestClient)
                .header(RoleUsersHeaders.role_user_header)

                .when()
                .delete("/{id}" ,"1")


                .then()
                .statusCode(FORBIDDEN.value())
        ;

    }

    @Test
    public void delete_ERROR() {
        RestAssuredWebTestClient
                .given()
                .webTestClient(webTestClient)
                .header(RoleUsersHeaders.role_user_header)

                .when()
                .delete("/{id}" ,"100")


                .then()
                .statusCode(FORBIDDEN.value())
        ;
    }

    @Test
    public void blockHoundWorks() {
        try {
            FutureTask<?> task = new FutureTask<>(() -> {
                Thread.sleep(0);
                return "";
            });

            Schedulers.parallel().schedule(task);

            task.get(10 ,TimeUnit.SECONDS);
            Assert.fail("should fail");
        } catch (ExecutionException | InterruptedException | TimeoutException e) {
            Assert.assertTrue("detected" ,e.getCause() instanceof BlockingOperationError);
        }
    }
}

/* JAMAIS DELETAR!!!!!!!

java.lang.IllegalStateException: You haven't configured a WebTestClient instance. You can do this statically

RestAssuredWebTestClient.mockMvc(..)
RestAssuredWebTestClient.standaloneSetup(..);
RestAssuredWebTestClient.webAppContextSetup(..);

or using the DSL:

given().
		mockMvc(..). ..
 */

//    @Autowired
//    private ApplicationContext applicationContext;
//
//    @Autowired
//    private WebApplicationContext webApplicationContext;
//    @Test
//    public void get_Webcontext() {
//        //        testClient = WebTestClient.bindToController(controller).build();
//        testClient = WebTestClient.bindToApplicationContext(applicationContext)
//                .configureClient()
//                .build();
//
//        RestAssuredWebTestClient
//                .given()
//                .webTestClient(testClient)
//
//                .when()
//                .get()
//
//                .then()
//                .statusCode(OK.value())
//
//                .body("name" ,hasItem("GLAUCO"))
//        ;
//    }
//
//    @Test
//    public void get_StandAlone() {
////        RestAssuredWebTestClient.standaloneSetup(new AnimeController());
//        RestAssuredWebTestClient.webAppContextSetup(webApplicationContext);
//        RestAssuredWebTestClient
//                .given()
////                .standaloneSetup(new AnimeController())
//
//                .when()
//                .get()
//
//                .then()
//                .statusCode(OK.value())
//
//                .body("name" ,hasItem("paulo"))
//        ;
//    }
