Napdroid
=================
A simple JSON / REST interface library for Android applications. Works with Android 2.2 and later.

Install
-------
Just copy the /bin/napdroid.jar file into your Android project and add it to your build path. 	
	
If you'd like to build it yourself, just load the project in Eclipse with the Android SDK installed and it will auto-generate the JAR file for you since the project is marked as a library.

For compilation outside of Eclipse, you can use the included Ant script.

	ant build.xml

This will build napdroid.jar. Be sure to specify the location of your Android SDK Jar in the build.xml file first.   

Example without parameters
--------------------------
	// Retrieve the JSON response to a simple REST call
	JSONClient client = JSONClient.getInstance("http://api.fogstack.com/myAPI");
	JSONObject result = client.get();

Example with parameters
-----------------------

	// Set up our parameters
	Map<String, String> params = new HashMap<String, String>();
	params.put("param", "value");
	
	// make your request
	JSONClient client = JSONClient.getInstance("http://api.fogstack.com/myAPI");
	JSONObject result = client.get(params);

You can continue using the same client with different parameter configurations.

FAQ:
-------
Why do we need a JSON/REST interface library for Android?

You don't really, but it's far too complex to retrieve a simple JSON object using the built in Android libraries. 
This just makes it easier for network-intensive applications.

COPYRIGHT
---------
Copyright (c) 2011 Fogstack LLC 
Sean Byrnes <sean@fogstack.com>
Released under the MIT license so you can use it for whatever you like. 