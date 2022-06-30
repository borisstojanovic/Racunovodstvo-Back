package raf.si.racunovodstvo.preduzece.integration.annotations;

import org.junit.jupiter.api.extension.ExtendWith;
import raf.si.racunovodstvo.preduzece.integration.extensions.MySQLExtension;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@ExtendWith(MySQLExtension.class)
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface MySQL {

}
