package com.awesomegroup.fridge;

import com.awesomegroup.fridge.favourite.FavouriteRecipe;
import com.awesomegroup.fridgeIngredient.FridgeIngredient;
import com.awesomegroup.fridgeIngredient.FridgeIngredientJson;
import com.awesomegroup.ingredients.Ingredient;
import com.awesomegroup.ingredients.IngredientsRepository;
import com.awesomegroup.recipe.Recipe;
import com.awesomegroup.recipe.RecipeRepository;
import com.awesomegroup.user.User;
import com.awesomegroup.user.UserRepository;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.security.Principal;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.when;

/**
 * Created by Michał on 2017-06-11.
 */
public class FridgeServiceTests {

    @Mock
    private FridgeRepository fridgeRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private IngredientsRepository ingredientsRepository;

    @Mock
    private RecipeRepository recipeRepository;

    @InjectMocks
    private FridgeService service;

    private Principal basePrincipal;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        basePrincipal = () -> "testUser";
    }

    @Test
    public void callAddIngredients_shouldAddAllIngredients() {
        List<Ingredient> ingredients = Collections.singletonList(
                Ingredient.create().name("Test").build()
        );

        Fridge f = Fridge.create().build();
        User user = User.create().email("testUser").fridge(f).build();

        //mocks
        when(ingredientsRepository.findByName("Test")).thenReturn(Optional.of(Ingredient.create().name("Test").build()));
        when(userRepository.findUserByEmail("testUser")).thenReturn(Optional.of(user));
        when(fridgeRepository.save(user.getFridge())).thenReturn(user.getFridge());

        assertThat(user.getFridge(), notNullValue());
        assertThat(user.getFridge().getFridgeIngredients().size(), is(0));

        boolean added = service.addFridgeIngredientsForUser(basePrincipal, ingredients);

        assertThat(added, is(true));
        assertThat(user.getFridge().getFridgeIngredients().size(), is(1));
        assertThat(user.getFridge().getFridgeIngredients().get(0).getIngredient().getIngredientName(), is("Test"));
    }

    @Test
    public void callAddIngredients_nullPrincipal_shouldReturnFalse() {
        List<Ingredient> ingredients = Collections.singletonList(
                Ingredient.create().name("Test").build()
        );

        boolean result = service.addFridgeIngredientsForUser(null, ingredients);

        assertThat(result, is(false));
    }

    @Test
    public void callAddIngredients_emptyList_shouldReturnFalse() {
        List<Ingredient> ingredients = Collections.emptyList();
        boolean result = service.addFridgeIngredientsForUser(basePrincipal, ingredients);
        assertThat(result, is(false));
    }

    @Test
    public void callAddIngredients_nullList_shouldReturnFalse() {
        boolean result = service.addFridgeIngredientsForUser(basePrincipal, null);
        assertThat(result, is(false));
    }

    @Test
    public void callAddIngredients_notExistingUser_shouldReturnFalse() {
        List<Ingredient> ingredients = Collections.singletonList(
                Ingredient.create().name("Test").build()
        );

        when(userRepository.findUserByEmail(basePrincipal.getName())).thenReturn(Optional.empty());

        boolean result = service.addFridgeIngredientsForUser(basePrincipal, ingredients);
        assertThat(result, is(false));
    }

    @Test
    public void callGetCurrentIngredients_userExists_shouldReturnListWithOneIngredient() {
        Fridge f = Fridge.create().build();
        f.getFridgeIngredients().add(
                FridgeIngredient.create().ingredient(
                Ingredient.create().name("Ingredient 1").build()).expires(LocalDate.now()).build());
        User user = User.create().email("testUser").fridge(f).build();

        when(userRepository.findUserByEmail(basePrincipal.getName())).thenReturn(Optional.of(user));

        List<FridgeIngredientJson> ingredients = service.getCurrentIngredients(basePrincipal);
        assertThat(ingredients.size(), is(1));
        assertThat(ingredients.get(0).getIngredientName(), is("Ingredient 1"));
    }

    @Test
    public void callGetCurrentIngredients_userDoesNotExist_shouldReturnEmptyList() {
        when(userRepository.findUserByEmail(basePrincipal.getName())).thenReturn(Optional.empty());
        List<FridgeIngredientJson> ingredients = service.getCurrentIngredients(basePrincipal);
        assertThat(ingredients.isEmpty(), is(true));
    }

    @Test
    public void callAddRecipeToFavourites_favouriteNotIncludedCurrently_shouldAddCorrectly() {
        Fridge f = Fridge.create().build();
        Recipe recipe = Recipe.create().name("Recipe 1").build();
        User user = User.create().email("testUser").fridge(f).build();
        when(userRepository.findUserByEmail(basePrincipal.getName())).thenReturn(Optional.of(user));
        when(recipeRepository.findRecipeByName(recipe.getName())).thenReturn(recipe);

        boolean result = service.addRecipeToFavourites(basePrincipal, recipe);

        assertThat(result, is(true));
        assertThat(user.getFridge().getFavouriteRecipes().size(), is(1));
        assertThat(user.getFridge().getFavouriteRecipes().get(0).getRecipe().getName(), is("Recipe 1"));
    }

    @Test
    public void callAddRecipeToFavourites_favouriteIncludedCurrently_shouldReturnFalse() {
        Fridge f = Fridge.create().build();
        Recipe recipe = Recipe.create().name("Recipe 1").build();
        f.getFavouriteRecipes().add(FavouriteRecipe.create().rating(0).recipe(recipe).build());
        User user = User.create().email("testUser").fridge(f).build();

        when(userRepository.findUserByEmail(basePrincipal.getName())).thenReturn(Optional.of(user));
        when(recipeRepository.findRecipeByName(recipe.getName())).thenReturn(recipe);

        boolean result = service.addRecipeToFavourites(basePrincipal, recipe);

        assertThat(result, is(false));
        assertThat(user.getFridge().getFavouriteRecipes().size(), is(1));
        assertThat(user.getFridge().getFavouriteRecipes().get(0).getRecipe().getName(), is("Recipe 1"));
    }

    @Test
    public void callAddRecipeToFavourites_nullRecipe_shouldReturnFalse() {
        boolean result = service.addRecipeToFavourites(basePrincipal, null);
        assertThat(result, is(false));
    }

    @Test
    public void callRemoveRecipeFromFavourites_recipeIncludedCurrently_shouldRemoveCorrectly() {
        Fridge f = Fridge.create().build();
        Recipe recipe = Recipe.create().name("Recipe 1").build();
        f.getFavouriteRecipes().add(FavouriteRecipe.create().recipe(recipe).rating(0).build());
        User user = User.create().email("testUser").fridge(f).build();
        when(userRepository.findUserByEmail(basePrincipal.getName())).thenReturn(Optional.of(user));
        when(recipeRepository.findRecipeByName(recipe.getName())).thenReturn(recipe);

        assertThat(user.getFridge().getFavouriteRecipes().size(), is(1));

        boolean result = service.removeRecipeFromFavourites(basePrincipal, recipe);

        assertThat(result, is(true));
        assertThat(user.getFridge().getFavouriteRecipes().size(), is(0));
    }

    @Test
    public void callRemoveRecipeFromFavourites_recipeNotIncludedCurrently_shouldReturnFalse() {
        Fridge f = Fridge.create().build();
        Recipe recipe = Recipe.create().name("Recipe 1").build();
        User user = User.create().email("testUser").fridge(f).build();

        when(userRepository.findUserByEmail(basePrincipal.getName())).thenReturn(Optional.of(user));
        when(recipeRepository.findRecipeByName(recipe.getName())).thenReturn(recipe);

        assertThat(user.getFridge().getFavouriteRecipes().size(), is(0));

        boolean result = service.removeRecipeFromFavourites(basePrincipal, recipe);

        assertThat(result, is(false));
    }

    @Test
    public void callRemoveRecipeFromFavourites_nullRecipe_shouldReturnFalse() {
        boolean result = service.removeRecipeFromFavourites(basePrincipal, null);
        assertThat(result, is(false));
    }

    @Test
    public void callGetAllRecipiesOfRating_shouldReturnListWith1Recipe() {
        Fridge f = Fridge.create().build();
        Recipe recipe = Recipe.create().name("Recipe 1").build();
        Recipe recipe2 = Recipe.create().name("Recipe 2").build();

        f.getFavouriteRecipes().add(FavouriteRecipe.create().recipe(recipe).rating(4f).build());
        f.getFavouriteRecipes().add(FavouriteRecipe.create().recipe(recipe2).rating(3.5f).build());
        User user = User.create().email("testUser").fridge(f).build();

        when(userRepository.findUserByEmail(basePrincipal.getName())).thenReturn(Optional.of(user));

        assertThat(user.getFridge().getFavouriteRecipes().size(), is(2));

        List<Recipe> recipes = service.getAllRecipesOfRating(basePrincipal, 3.5f);
        assertThat(recipes, is(notNullValue()));
        assertThat(recipes.size(), is(1));
        assertThat(recipes.get(0).getName(), is("Recipe 2"));
    }

    @Test
    public void callGetAllRecipesOfRating_noRecipeWithGivenRating_shouldReturnEmptyList() {
        Fridge f = Fridge.create().build();
        Recipe recipe = Recipe.create().name("Recipe 1").build();
        Recipe recipe2 = Recipe.create().name("Recipe 2").build();

        f.getFavouriteRecipes().add(FavouriteRecipe.create().recipe(recipe).rating(4f).build());
        f.getFavouriteRecipes().add(FavouriteRecipe.create().recipe(recipe2).rating(3.5f).build());
        User user = User.create().email("testUser").fridge(f).build();

        when(userRepository.findUserByEmail(basePrincipal.getName())).thenReturn(Optional.of(user));

        assertThat(user.getFridge().getFavouriteRecipes().size(), is(2));

        List<Recipe> recipes = service.getAllRecipesOfRating(basePrincipal, 5f);
        assertThat(recipes, is(notNullValue()));
        assertThat(recipes.size(), is(0));
    }

    @Test
    public void callChangeRecipeRating_shouldChangeRecipeRatingAndReturnTrue() {
        Fridge f = Fridge.create().build();
        Recipe recipe = Recipe.create().name("Recipe 1").build();

        f.getFavouriteRecipes().add(FavouriteRecipe.create().recipe(recipe).rating(4f).build());
        User user = User.create().email("testUser").fridge(f).build();

        when(userRepository.findUserByEmail(basePrincipal.getName())).thenReturn(Optional.of(user));

        assertThat(user.getFridge().getFavouriteRecipes().size(), is(1));
        assertThat(user.getFridge().getFavouriteRecipes().get(0).getRating(), is(4f));

        boolean result = service.changeRecipeRating(basePrincipal, recipe, 5f);

        assertThat(result, is(true));
        assertThat(user.getFridge().getFavouriteRecipes(), is(notNullValue()));
        assertThat(user.getFridge().getFavouriteRecipes().size(), is(1));
        assertThat(user.getFridge().getFavouriteRecipes().get(0).getRating(), is(5f));
    }

    @Test
    public void callRemoveFridgeIngredients_shouldRemoveIngredient() {
        Ingredient ingredient = Ingredient.create().name("test1").build();
        Fridge f = Fridge.create().build();
        f.getFridgeIngredients().add(FridgeIngredient.create().ingredient(ingredient).build());
        User user = User.create().email("testUser").fridge(f).build();
        when(userRepository.findUserByEmail(basePrincipal.getName())).thenReturn(Optional.of(user));
        when(ingredientsRepository.findByName(ingredient.getIngredientName())).thenReturn(Optional.of(ingredient));

        assertThat(f.getFridgeIngredients().size(), is(1));
        assertThat(f.getFridgeIngredients().get(0).getIngredient().getIngredientName(), is("test1"));

        boolean result = service.removeFridgeIngredientForUser(basePrincipal, FridgeIngredientJson.create().name("test1").build());

        assertThat(result, is(true));
        assertThat(user.getFridge().getFridgeIngredients().size(), is(0));

    }

}
