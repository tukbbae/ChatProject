/**
 * 공통 Javascript
 *
 * Created by kyeongjinkim on 2016. 4. 13..
 */

// Message properties 파일 설정
jQuery.i18n.properties({
    name : "validate",
    language : "ko",
    path : "/i18n/",
    mode : "map"
});

// Translated default messages for the jQuery validation plugin.
jQuery.extend(jQuery.validator.messages, {
    required : jQuery.i18n.prop("validate.common.required"),
    remote : jQuery.i18n.prop("validate.common.remote"),
    email : jQuery.i18n.prop("validate.common.email"),
    url : jQuery.i18n.prop("validate.common.url"),
    date : jQuery.i18n.prop("validate.common.date"),
    dateISO : jQuery.i18n.prop("validate.common.dateISO"),
    number : jQuery.i18n.prop("validate.common.number"),
    digits : jQuery.i18n.prop("validate.common.digits"),
    creditcard : jQuery.i18n.prop("validate.common.creditcard"),
    equalTo : jQuery.i18n.prop("validate.common.equalTo"),
    accept : jQuery.i18n.prop("validate.common.accept"),
    maxlength : jQuery.i18n.prop("validate.common.maxlength"),
    minlength : jQuery.i18n.prop("validate.common.minlength"),
    rangelength : jQuery.i18n.prop("validate.common.rangelength"),
    range : jQuery.i18n.prop("validate.common.range"),
    max : jQuery.i18n.prop("validate.common.max"),
    min : jQuery.i18n.prop("validate.common.min")
});

// 현재 메뉴 활성화
$(document).ready(function () {
    var currentMenuLink = $('a[href="' + this.location.pathname + '"]');

    currentMenuLink.parent().addClass('current');
});