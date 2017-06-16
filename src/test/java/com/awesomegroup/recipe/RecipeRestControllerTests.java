package com.awesomegroup.recipe;

import com.awesomegroup.ingredients.IngredientsService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestContext;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Created by Michał on 2017-04-14.
 */
@SpringBootTest
@ContextConfiguration(classes = {TestContext.class})
@RunWith(SpringJUnit4ClassRunner.class)
@AutoConfigureMockMvc
public class RecipeRestControllerTests {

    private MockMvc mockMvc;

    @MockBean
    private RecipeService recipeService;

    @MockBean
    private IngredientsService ingredientsService;

    @Before
    public void startUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(new RecipeRestController(recipeService, ingredientsService)).build();
    }

    @Test
    public void callFindAll_shouldReturn2Elements() throws Exception {
        /*
         * Initialization step
         */
        List<Recipe> mockRecipes = new ArrayList<>();
        mockRecipes.add(Recipe.create().id(1L).difficulty(RecipeDifficulty.MEDIUM)
                                .name("Mock recipe 1").preparationTime((short) 25).servings((byte) 2).build());

        mockRecipes.add(Recipe.create().id(2L).difficulty(RecipeDifficulty.EASY)
                .name("Mock recipe 2").preparationTime((short) 10).servings((byte) 1).build());

        /*
        Mocking step
         */
        when(recipeService.getAllRecipesPaged(new PageRequest(0, RecipeService.RECIPES_PER_PAGE))).thenReturn(mockRecipes);
        when(recipeService.getCount()).thenReturn(1);

        /*
        Test
         */
        mockMvc.perform(get("/api/recipe-0"))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[0].recipe_name", is("Mock recipe 1")))
                .andExpect(jsonPath("$[0].prepTime", is(25)))
                .andExpect(jsonPath("$[0].servings", is(2)))
                .andExpect(jsonPath("$[1].id", is(2)))
                .andExpect(jsonPath("$[1].recipe_name", is("Mock recipe 2")))
                .andExpect(jsonPath("$[1].prepTime", is(10)))
                .andExpect(jsonPath("$[1].servings", is(1)));
    }

    @Test
    public void callFindAllByDifficulty_MEDIUM_shouldReturn1Element() throws Exception {
        List<Recipe> mockRecipes = new ArrayList<>();
        mockRecipes.add(Recipe.create().id(1L).difficulty(RecipeDifficulty.MEDIUM)
                .name("Mock recipe 1").preparationTime((short) 25).servings((byte) 2).build());
        mockRecipes.add(Recipe.create().id(2L).difficulty(RecipeDifficulty.EASY)
                .name("Mock recipe 2").preparationTime((short) 10).servings((byte) 1).build());

        when(recipeService.getByDifficulty(RecipeDifficulty.MEDIUM, new PageRequest(0, RecipeService.RECIPES_PER_PAGE)))
                          .thenReturn(mockRecipes.stream().filter(recipe -> recipe.getDifficulty() == RecipeDifficulty.MEDIUM)
                                                          .collect(Collectors.toList()));
        when(recipeService.getCount()).thenReturn(1);

        mockMvc.perform(get("/api/recipe/difficulty-medium/0"))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[0].recipe_name", is("Mock recipe 1")))
                .andExpect(jsonPath("$[0].prepTime", is(25)))
                .andExpect(jsonPath("$[0].servings", is(2)));
    }

    @Test
    public void callGetRecipeByName_shouldReturn1Element_MockRecipe1() throws Exception {
        Recipe mockRecipe = Recipe.create().id(1L).difficulty(RecipeDifficulty.MEDIUM)
                .name("Mock recipe 1").preparationTime((short) 25).servings((byte) 2).build();

        when(recipeService.getRecipeByName("mock-recipe-1")).thenReturn(mockRecipe);
        when(recipeService.getCount()).thenReturn(1);

        mockMvc.perform(get("/api/recipe/mock-recipe-1"))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.recipe_name", is("Mock recipe 1")))
                .andExpect(jsonPath("$.prepTime", is(25)))
                .andExpect(jsonPath("$.servings", is(2)));
    }

    @Test
    public void callAddRecipe_shouldReturnStatusOk() throws Exception {
        Recipe mockRecipe = Recipe.create().id(1L).difficulty(RecipeDifficulty.MEDIUM)
                .name("Mock recipe 1").preparationTime((short) 25).servings((byte) 2).build();

        ObjectMapper objectMapper = new ObjectMapper();
        String mockRecipeAsString = objectMapper.writeValueAsString(mockRecipe);

        /*
         * Void method mocking
         */
        doNothing().when(recipeService).saveRecipe(mockRecipe);

        LoggerFactory.getLogger(RecipeRestControllerTests.class).info(mockRecipeAsString);

        mockMvc.perform(post("/api/recipe/add")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"recipe_name\":\"Mock recipe 1\",\"difficulty\":\"MEDIUM\",\"recipeIngredients\":[],\"id\":1}")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

}
