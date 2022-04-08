package rs.raf.demo.utils;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import rs.raf.demo.exceptions.OperationNotSupportedException;
import rs.raf.demo.specifications.RacunSpecification;
import rs.raf.demo.specifications.RacunSpecificationsBuilder;

import static org.junit.jupiter.api.Assertions.*;

class SearchUtilTest {

    private SearchUtil searchUtil;

    @BeforeEach
    void setUp() {
        this.searchUtil = new SearchUtil();
    }

    @Test
    void testHappyPath() {
        String search = "datumKnjizenja>10000";

        RacunSpecification racunSpecification = (RacunSpecification)searchUtil.getSpec(search);
        RacunSpecificationsBuilder builder = new RacunSpecificationsBuilder();
        builder.with("datumKnjizenja",">","10000");
        assertEquals(builder.build(), racunSpecification);
    }

    @Test
    void testGreaterOrEqualNotSupported() {
        String search = "datumKnjizenja<=10000";

        RacunSpecification racunSpecification = (RacunSpecification)searchUtil.getSpec(search);
        RacunSpecificationsBuilder builder = new RacunSpecificationsBuilder();
        builder.with("datumKnjizenja","<","=10000");
        assertEquals(builder.build(), racunSpecification);
    }

    @Test
    void testNotEqualNotSupported() {
        String search = "datumKnjizenja!=10000";

        assertThrows(OperationNotSupportedException.class, () -> searchUtil.getSpec(search));
    }

    @Test
    void testWrongEqualSymbol() {
        String search = "datumKnjizenja=10000";

        assertThrows(OperationNotSupportedException.class, () -> searchUtil.getSpec(search));
    }

    @Test
    void testEmptySearch() {
        String search = " ";

        assertThrows(OperationNotSupportedException.class, () -> searchUtil.getSpec(search));
    }
}