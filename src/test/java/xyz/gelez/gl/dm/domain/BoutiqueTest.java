package xyz.gelez.gl.dm.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import xyz.gelez.gl.dm.web.rest.TestUtil;

class BoutiqueTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Boutique.class);
        Boutique boutique1 = new Boutique();
        boutique1.setId(1L);
        Boutique boutique2 = new Boutique();
        boutique2.setId(boutique1.getId());
        assertThat(boutique1).isEqualTo(boutique2);
        boutique2.setId(2L);
        assertThat(boutique1).isNotEqualTo(boutique2);
        boutique1.setId(null);
        assertThat(boutique1).isNotEqualTo(boutique2);
    }
}
