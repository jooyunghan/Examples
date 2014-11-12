package com.lge.lazy;

import org.junit.Test;
import static com.lge.lazy.Stream.*;
import static org.junit.Assert.assertEquals;

/**
 * Created by jooyung.han on 11/12/14.
 */
public class StreamTest {
    @Test
    public void streamEquals() {
        assertEquals(stream(1, 2, 3), stream(1, 2, 3));
    }
}
