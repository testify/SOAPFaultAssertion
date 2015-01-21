/*
 * Copyright 2015 Codice Foundation
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package SOAPFault;

import org.codice.testify.assertions.Assertion;
import org.codice.testify.objects.AssertionStatus;
import org.codice.testify.objects.TestifyLogger;
import org.codice.testify.objects.Response;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

/**
 * The SOAPFaultAssertion class is a Testify Assertion service that checks if a soap fault was returned in the processor response
 */
public class SOAPFaultAssertion implements BundleActivator, Assertion {

    @Override
    public AssertionStatus evaluateAssertion(String assertionInfo, Response response) {

        TestifyLogger.debug("Running SOAPFaultAssertion", this.getClass().getSimpleName());

        //Get the processor response
        String responseValue = response.getResponse();
        AssertionStatus status;

        //If the response from the processor is null, return a failure
        if (responseValue == null) {
            status = new AssertionStatus("No processor response");
        } else {

            //If the response contains a soap fault, return failure details of null meaning a successful assertion
            if (responseValue.contains("<soap:Fault>") && responseValue.contains("</soap:Fault>")) {
                status = new AssertionStatus(null);

            } else {

                //If the response does not contain a soap fault, return a failure
                status = new AssertionStatus("Response does not contain a SOAP fault");
            }
        }
        return status;
    }

    @Override
    public void start(BundleContext bundleContext) throws Exception {

        //Register the SOAPFault service
        bundleContext.registerService(Assertion.class.getName(), new SOAPFaultAssertion(), null);

    }

    @Override
    public void stop(BundleContext bundleContext) throws Exception {

    }
}