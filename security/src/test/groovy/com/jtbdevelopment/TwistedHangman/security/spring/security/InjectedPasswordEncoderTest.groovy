package com.jtbdevelopment.TwistedHangman.security.spring.security
/**
 * Date: 12/24/14
 * Time: 4:53 PM
 */
class InjectedPasswordEncoderTest extends GroovyTestCase {
    InjectedPasswordEncoder encoder = new InjectedPasswordEncoder()

    void testEncryption() {
        def PASSWORD = "PASSWORD"
        def ENCODED = encoder.encode(PASSWORD)
        assert encoder.matches(PASSWORD, ENCODED)
        assert !encoder.matches(PASSWORD + "X", ENCODED)
        assert !encoder.matches(PASSWORD, ENCODED + "X")
    }
}
