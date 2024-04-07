package persistence.sql.ddl;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import persistence.EntityMetaDataTestSupport;
import persistence.sql.dialect.Dialect;
import persistence.sql.dialect.H2Dialect;
import persistence.sql.mapping.ColumnBinder;
import persistence.sql.mapping.ColumnTypeMapper;
import persistence.sql.mapping.Table;
import persistence.sql.mapping.TableBinder;

import static org.assertj.core.api.Assertions.assertThat;

class DefaultDdlQueryBuilderTest extends EntityMetaDataTestSupport {

    private final TableBinder tableBinder = new TableBinder();

    private final ColumnTypeMapper columnTypeMapper = ColumnTypeMapper.getInstance();

    private final ColumnBinder columnBinder = new ColumnBinder(columnTypeMapper);

    private final Dialect dialect = new H2Dialect();
    private final DdlQueryBuilder queryBuilder = new DefaultDdlQueryBuilder(dialect);

    @DisplayName("Entity 의 기본 JAVA 객체 정보만을 가지고 create 쿼리를 만든다")
    @Test
    public void buildCreateQueryV1() throws Exception {
        // given
        final Class<PersonV1> clazz = PersonV1.class;
        final Table table = tableBinder.createTable(clazz);

        final String ddl = "create table PersonV1 (\n" +
                "    id bigint,\n" +
                "    name varchar(255),\n" +
                "    age integer,\n" +
                "    primary key (id)\n" +
                ")";

        // when
        final String createQuery = queryBuilder.buildCreateQuery(table);

        // then
        assertThat(createQuery).isEqualTo(ddl);
    }

    @DisplayName("Entity 의 @Column, @GeneratedValue 정보를 추가로 create 쿼리를 만든다")
    @Test
    public void buildCreateQueryV2() throws Exception {
        // given
        final Class<PersonV2> clazz = PersonV2.class;
        final Table table = tableBinder.createTable(clazz);

        final String ddl = "create table PersonV2 (\n" +
                "    id bigint generated by default as identity,\n" +
                "    nick_name varchar(255),\n" +
                "    old integer,\n" +
                "    email varchar(255) not null,\n" +
                "    primary key (id)\n" +
                ")";

        // when
        final String createQuery = queryBuilder.buildCreateQuery(table);

        // then
        assertThat(createQuery).isEqualTo(ddl);
    }

    @DisplayName("Entity 의 @Table, @Transient 정보를 추가로 create 쿼리를 만든다")
    @Test
    public void buildCreateQueryV3() throws Exception {
        // given
        final Class<PersonV3> clazz = PersonV3.class;
        final Table table = tableBinder.createTable(clazz);

        final String ddl = "create table users (\n" +
                "    id bigint generated by default as identity,\n" +
                "    nick_name varchar(255),\n" +
                "    old integer,\n" +
                "    email varchar(255) not null,\n" +
                "    primary key (id)\n" +
                ")";

        // when
        final String createQuery = queryBuilder.buildCreateQuery(table);

        // then
        assertThat(createQuery).isEqualTo(ddl);
    }

    @DisplayName("Entity 의 기본 JAVA 객체 정보만을 가지고 drop 쿼리를 만든다")
    @Test
    public void buildPersonV1DropQuery() throws Exception {
        // given
        final Class<PersonV1> clazz = PersonV1.class;
        final Table table = tableBinder.createTable(clazz);

        final String ddl = "drop table if exists PersonV1;";

        // when
        final String dropQuery = queryBuilder.buildDropQuery(table);

        // then
        assertThat(dropQuery).isEqualTo(ddl);
    }

    @DisplayName("Entity @Table 정보로 drop 쿼리를 만든다")
    @Test
    public void buildPersonV3DropQuery() throws Exception {
        // given
        final Class<PersonV3> clazz = PersonV3.class;
        final Table table = tableBinder.createTable(clazz);

        final String ddl = "drop table if exists users;";

        // when
        final String dropQuery = queryBuilder.buildDropQuery(table);

        // then
        assertThat(dropQuery).isEqualTo(ddl);
    }

}
