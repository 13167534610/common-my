package com.common.datasourceconfig.config;

/**
 * @description: mybatis配置信息
 * @author: wangqiang
 * @create: 2020-04-03 11:36:09
 */
public class MybatisConfig {
    /*@Configuration
    @MapperScan(basePackages = "com.zhx.giftcardshop", sqlSessionTemplateRef = "sqlSessionTemplateGf")
    public static class Db1 {

        @Bean(name = "sqlSessionFactoryGf")
        public SqlSessionFactory sqlSessionFactory1(@Qualifier("dataSourceGf") DataSource dataSource) throws Exception {
            SqlSessionFactoryBean factoryBean = new SqlSessionFactoryBean();
            factoryBean.setDataSource(dataSource);

            factoryBean.setMapperLocations(new PathMatchingResourcePatternResolver().getResources("classpath*:mapper/giftcardshop/*.xml"));
            return factoryBean.getObject();
        }

        @Bean(name = "sqlSessionTemplateGf")
        public SqlSessionTemplate sqlSessionTemplate1(@Qualifier("sqlSessionFactoryGf") SqlSessionFactory sqlSessionFactory) throws Exception {
            return new SqlSessionTemplate(sqlSessionFactory);
        }

        @Bean(name = "dataSourceTransactionManagerGf")
        public DataSourceTransactionManager dataSourceTransactionManager1(@Qualifier("dataSourceGf") DataSource dataSource) {
            return new DataSourceTransactionManager(dataSource);
        }
    }

    @Configuration
    @MapperScan(basePackages = "com.zhx.xshtest", sqlSessionTemplateRef = "sqlSessionTemplateXshtest")
    public static class Db2 {

        @Bean(name = "sqlSessionFactoryXshtest")
        public SqlSessionFactory sqlSessionFactory2(@Qualifier("dataSourceXshtest") DataSource dataSource) throws Exception {
            SqlSessionFactoryBean factoryBean = new SqlSessionFactoryBean();
            factoryBean.setDataSource(dataSource);
            factoryBean.setMapperLocations(new PathMatchingResourcePatternResolver().getResources("classpath*:mapper/xshtest/*.xml"));
            return factoryBean.getObject();
        }

        @Bean(name = "sqlSessionTemplateXshtest")
        public SqlSessionTemplate sqlSessionTemplate2(@Qualifier("sqlSessionFactoryXshtest") SqlSessionFactory sqlSessionFactory) throws Exception {
            return new SqlSessionTemplate(sqlSessionFactory);
        }

        @Bean(name = "dataSourceTransactionManagerXshtest")
        public DataSourceTransactionManager dataSourceTransactionManager2(@Qualifier("dataSourceXshtest") DataSource dataSource) {
            return new DataSourceTransactionManager(dataSource);
        }
    }*/
}
