package com.soukuan.base

import com.alibaba.fastjson.JSONObject
import com.soukuan.domain.ResponseEntity
import org.junit.Before
import org.junit.runner.RunWith
import org.springframework.http.MediaType
import org.springframework.test.annotation.Rollback
import org.springframework.test.context.junit4.SpringRunner
import org.springframework.test.context.web.WebAppConfiguration
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.MvcResult
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import org.springframework.web.context.WebApplicationContext

import javax.annotation.Resource

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

/**
 * Title
 * Time 2017/7/10.
 * Version v1.0
 */
@RunWith(SpringRunner)
@WebAppConfiguration
@Rollback
abstract class GroovyBaseTest {

    @Resource
    protected WebApplicationContext context

    protected MockMvc mvc

    @Before
    void setupMockMvc() throws Exception {
        mvc = MockMvcBuilders.webAppContextSetup(context).build()
    }

    /**
     * 请求uri地址并返回接口返回数据ean
     *
     * @param uri 接口uri地址
     * @return 接口返回原始数据
     */
    protected String apiTest(String uri, def params) throws Exception {
        return apiTest(uri, params, null)
    }

    /**
     * 请求uri地址并返回接口返回数据ean
     *
     * @param uri 接口uri地址
     * @return 接口返回原始数据
     */
    protected String apiTest(String uri, def params, String content) throws Exception {
        MockHttpServletRequestBuilder mock = MockMvcRequestBuilders.post(uri.toString())
        if(params){
            Map<String, Object> mapParams
            if(params instanceof Map){
                mapParams = params
            }else{
                mapParams = JSONObject.toJSON(params) as Map
            }
            for (Map.Entry<String, Object> entry : mapParams.entrySet()) {
                mock.param(entry.getKey().toString(), entry.getValue().toString())
            }
        }
        if(content){
            mock.contentType(MediaType.APPLICATION_JSON_UTF8)
            mock.content(content)
        }
        MvcResult result = mvc.perform(mock).andExpect(status().isOk()).andReturn()
        return result.getResponse().getContentAsString()
    }

    /**
     * 通常使用的测试接口方法。
     * 该方法会请求uri地址，并校验返回参数retCode是否是200，retMesg是否是空，并返回retData中的数据
     *
     * @param uri 接口uri地址
     * @return retData中的数据
     * @throws Exception
     */
    protected Object apiTestAssertSuccess(String uri, def params) throws Exception {
        return this.apiTestAssertSuccess(uri, params, null)
    }

    /**
     * 通常使用的测试接口方法。
     * 该方法会请求uri地址，并校验返回参数retCode是否是非200
     *
     * @param uri 接口uri地址
     * @return retData中的数据
     * @throws Exception
     */
    protected String apiTestAssertFail(String uri, def params) throws Exception {
        return this.apiTestAssertFail(uri, params, null)
    }

    /**
     * 通常使用的测试接口方法。
     * 该方法会请求uri地址，并校验返回参数retCode是否是非200
     *
     * @param uri 接口uri地址
     * @return retData中的数据
     * @throws Exception
     */
    protected String apiTestAssertFail(String uri, def params, String content) throws Exception {
        ResponseEntity apiResult = apiTestParseApiResult(uri, params, content)
        assert apiResult.retCode != "200"
        return apiResult.retCode
    }

    /**
     * 通常使用的测试接口方法。
     * 该方法会请求uri地址，并校验返回参数retCode是否是200，retMesg是否是空，并返回retData中的数据
     *
     * @param uri 接口uri地址
     * @return retData中的数据
     * @throws Exception
     */
    protected Object apiTestAssertSuccess(String uri, def params, String content) throws Exception {
        ResponseEntity apiResult = apiTestParseApiResult(uri, params, content)
        assert apiResult.retCode == "200"
        assert !apiResult.retMesg
        return apiResult.retData
    }

    /**
     * 请求uri地址并返回对应的ResponseEntity对象
     *
     * @param uri 接口uri地址
     * @return ApiResult对象
     * @throws Exception
     */
    protected ResponseEntity apiTestParseApiResult(String uri, def params) throws Exception {
        return this.apiTestParseApiResult(uri, params, null)
    }

    /**
     * 请求uri地址并返回对应的ResponseEntity对象
     *
     * @param uri 接口uri地址
     * @return ApiResult对象
     * @throws Exception
     */
    protected ResponseEntity apiTestParseApiResult(String uri, def params, String content) throws Exception {
        String returnContent = this.apiTest(uri, params, content)
        return JSONObject.parseObject(returnContent, ResponseEntity.class)
    }
}