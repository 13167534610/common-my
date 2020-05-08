package com.common.dataconfig;

import com.common.dataconfig.annotation.MultiDataSourceTransactional;
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
