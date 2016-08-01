package com.hmlee.chatchat.core.base;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * BaseController
 *
 * Controller 클래스의 공통 속성을 정의 하는 상위 클래스
 *
 * Created by hmlee
 */
public abstract class BaseController {
    protected Logger logger = LoggerFactory.getLogger(this.getClass());
}
