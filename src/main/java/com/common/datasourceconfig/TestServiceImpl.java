package com.common.datasourceconfig;

import com.common.datasourceconfig.annotation.MultiDataSourceTransactional;
import org.springframework.stereotype.Service;

/**
 * @Description:
 * @Author: wangqiang
 * @Date:2020/5/8 14:07
 */
@Service
public class TestServiceImpl {

    @MultiDataSourceTransactional(transactionManagers = {"dataSourceTransactionManagerXshtest", "dataSourceTransactionManagerGf"})
    public void testService(){

    }
}
