package raf.si.racunovodstvo.knjizenje.relations;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import static java.lang.Long.parseLong;

public class DokumentRelations<T> extends RacunRelations<T>{

    protected Expression<String> brojDokumentaExpression;
    protected final String brojDokumenta;

    public DokumentRelations(Root<T> root, CriteriaBuilder builder, String key, String val) {
        super(root, builder, key, val);
        brojDokumentaExpression = root.get(key).as(String.class);
        brojDokumenta = val;
    }

    @Override
    public Predicate greaterThanOrEqualTo() {
        return this.builder.greaterThanOrEqualTo(brojDokumentaExpression,brojDokumenta);
    }

    @Override
    public Predicate lessThanOrEqualTo() {
        return this.builder.lessThanOrEqualTo(brojDokumentaExpression,brojDokumenta);
    }

    @Override
    public Predicate equalTo() {
        return this.builder.equal(brojDokumentaExpression,brojDokumenta);
    }
}
