package com.awesomegroup.fridge;

import com.awesomegroup.ingredients.Ingredient;
import com.awesomegroup.ingredients.IngredientsRepository;
import com.awesomegroup.user.User;
import com.awesomegroup.user.UserRepository;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.security.Principal;
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
        assertThat(user.getFridge().getFridgeIngredients().get(0).getIngredientName(), is("Test"));
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
        f.addIngredient(Ingredient.create().name("Ingredient 1").build());
        User user = User.create().email("testUser").fridge(f).build();

        when(userRepository.findUserByEmail(basePrincipal.getName())).thenReturn(Optional.of(user));

        List<Ingredient> ingredients = service.getCurrentIngredients(basePrincipal);
        assertThat(ingredients.size(), is(1));
        assertThat(ingredients.get(0).getIngredientName(), is("Ingredient 1"));
    }

    @Test
    public void callGetCurrentIngredients_userDoesNotExist_shouldReturnEmptyList() {
        when(userRepository.findUserByEmail(basePrincipal.getName())).thenReturn(Optional.empty());
        List<Ingredient> ingredients = service.getCurrentIngredients(basePrincipal);
        assertThat(ingredients.isEmpty(), is(true));
    }

}
