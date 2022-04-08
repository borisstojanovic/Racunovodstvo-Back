package rs.raf.demo.specifications;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.data.jpa.domain.Specification;

import java.lang.reflect.Field;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class RacunSpecificationsBuilderTest {

    private RacunSpecificationsBuilder builder;

    @BeforeEach
    void setUp() {
        this.builder = new RacunSpecificationsBuilder();
    }

    private List<SearchCriteria> getParams(RacunSpecificationsBuilder builder) throws NoSuchFieldException, IllegalAccessException {
        Field f = builder.getClass().getDeclaredField("params");
        f.setAccessible(true);
        return  (List<SearchCriteria>) f.get(builder);
    }

    @Test
    void testWith() throws NoSuchFieldException, IllegalAccessException {
        RacunSpecificationsBuilder newBuilder = builder.with("knjizenje", ":", "5");

        List<SearchCriteria> param = getParams(builder);
        List<SearchCriteria> newParam = getParams(newBuilder);
        assertEquals(builder, newBuilder);
        assertEquals(param, newParam);
    }

    @Test
    void testBuildNull() {
        Specification specification = builder.build();

        assertEquals(null, specification);
    }

    @Test
    void testBuild() {
        builder = builder.with("knjizenje", ":", "5");

        Specification result = new RacunSpecification(new SearchCriteria("knjizenje", ":", "5"));

        assertEquals(result, builder.build());
    }
}