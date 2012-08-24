package com.adobe.people.jedelson.cq.urlfilter.impl;

import static org.junit.Assert.*;

import java.util.HashMap;

import org.apache.sling.api.request.RequestPathInfo;
import org.apache.sling.api.resource.ValueMap;
import org.apache.sling.api.wrappers.ValueMapDecorator;
import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JMock;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.adobe.people.jedelson.cq.urlfilter.impl.UrlFilter;

@RunWith(JMock.class)
public class UrlFilterTest {

    private Mockery context = new JUnit4Mockery();
    private ValueMap properties;

    @Before
    public void setup() {
        properties = new ValueMapDecorator(new HashMap<String, Object>());
    }

    @Test
    public void null_selector() {
        UrlFilter filter = new UrlFilter();

        final RequestPathInfo testInfo = context.mock(RequestPathInfo.class);
        this.context.checking(new Expectations() {
            {
                allowing(testInfo).getSelectorString();
                will(returnValue(null));
            }
        });

        assertTrue(filter.checkSelector(testInfo, null));
    }

    @Test
    public void non_null_selector() {
        UrlFilter filter = new UrlFilter();

        final RequestPathInfo testInfo = context.mock(RequestPathInfo.class);
        this.context.checking(new Expectations() {
            {
                allowing(testInfo).getSelectorString();
                will(returnValue("sample"));
            }
        });

        // null allowedSelectors = ok
        assertTrue(filter.checkSelector(testInfo, properties));

        // empty array allowedSelectors = fail
        properties.put(UrlFilter.PN_ALLOWED_SELECTORS, (Object) new String[0]);
        assertFalse(filter.checkSelector(testInfo, properties));

        // selector string in array = ok
        properties.put(UrlFilter.PN_ALLOWED_SELECTORS, (Object) new String[] { "sample", "sample2" });
        assertTrue(filter.checkSelector(testInfo, properties));

        // selector string not in array = fail
        properties.put(UrlFilter.PN_ALLOWED_SELECTORS, (Object) new String[] { "other" });
        assertFalse(filter.checkSelector(testInfo, properties));
        
        properties.clear();
        
        // matches regex
        properties.put(UrlFilter.PN_ALLOWED_SELECTOR_PATTERN, "^s[a-z]m.*$");
        assertTrue(filter.checkSelector(testInfo, properties));

        // doesn't match regex
        properties.put(UrlFilter.PN_ALLOWED_SELECTOR_PATTERN, "^s[1-2]m$");
        assertFalse(filter.checkSelector(testInfo, properties));
        
        properties.clear();
        
        // matches array or regex = ok
        properties.put(UrlFilter.PN_ALLOWED_SELECTORS, (Object) new String[] { "other" });
        properties.put(UrlFilter.PN_ALLOWED_SELECTOR_PATTERN, "^s[a-z]m.*$");
        assertTrue(filter.checkSelector(testInfo, properties));
        
        properties.put(UrlFilter.PN_ALLOWED_SELECTORS, (Object) new String[] { "sample" });
        properties.put(UrlFilter.PN_ALLOWED_SELECTOR_PATTERN, "^s[a-z]m$");
        assertTrue(filter.checkSelector(testInfo, properties));

        
    }
}
