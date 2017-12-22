# Programming Assignment - 1

## Solution Design

The service consists of two parts: an Application Context and a Web service.

The Application Context is the core component and is responsible for the life cycle and storage of data objects (e.g. creating accounts) and for the operations that are carried out upon these objects (e.g. transfer requests).

The Web service is the point of access for the clients of the API and is responsible for translating HTTP REST requests into calls to the Application Context. It also defines the protocol of communication with the clients.

For simplicity and speed, and for the purpose of this assignment, it was chosen that no persistence layer will be used. When the application starts it contains no data, and when the application shuts down all data is lost.

The reliability and integrity of the system is verified with unit testing.
 	
## Technology Choices

For the development of the RESTful API, the Jersey framework was chosen. Jersey provides the reference implementation of the JAX-RS specification, and provides some useful facilities for developing RESTful Web Services, such as, HTTP request mapping into method calls, as well as JSON support.

For storing objects in memory, concurrent hash maps have been used. These allow linear time for adding and accessing objects, and are thread-safe.

For generating unique IDs for objects, atomic integers have been used. These guarantee that no two objects of the same type will be assigned the same ID.

Any data prone to race conditions, such as account balances, are protected with intrinsic locks on object level.

For unit testing the JUnit framework is used.

## Limitations / Known Issues

* No persistence 
* No access control layer
* No proper error reporting

## Building / Deployment Instructions

The service is a Java web application. To deploy it one can build and export it as a WAR file which can be used for deployment.

The service must be imported into a server with a servlet container. A popular choice of servlet-enabled server is Tomcat. Tomcat comes with a built-in servlet container (Catalina). Usually, the deployment process is as simple as copying the exported WAR file into the server's webapps directory, which is then picked up automatically.  

Some IDEs provide the option to export a project as a WAR file. However, if one needs to automate the deployment process, he/she can build and deploy the WAR file using command line, like this:

	$ cd /path/to/project
	$ jar -cvf service.war *
	$ cp service.war /path/to/tomcat/webapps

## Usage / Examples

All data from and to the RESTful API is encoded in JSON.

The base URL of the API's point of access is: http://localhost:8080/IngenicoAssignment

To create a new Account one has to make a POST request to http://localhost:8080/IngenicoAssignment/rest/Accounts and provide the following information in the request's body:

	{
		"name": [account holder's name],
		"balance": [initial balance]
	}

Initial balance must a be non-negative floating point number.

Upon success a 201 Created response and a "Location" header pointing to the URL of the newly created Account are returned.

To retrieve information about a specific Account one has to make a GET request to http://localhost:8080/IngenicoAssignment/rest/Accounts/{id} where {id} is the ID of the requested Account.

If the Account is found a 200 OK response is returned together with a JSON representation of the Account. If the Account is not found a 404 Not Found response is returned instead.

To retrieve all Account objects in the system one has to make a GET request to http://localhost:8080/IngenicoAssignment/rest/Accounts

A 200 OK response is returned together with a JSON representation of an array containing all Account objects.

To make a Transfer from one Account to another on has to to make a POST request to http://localhost:8080/IngenicoAssignment/rest/Transfers and provide the following information in the request's body:

	{
		"sourceAccountId" : [the ID of the source account],
		"destinationAccountId" : [the ID of the destination account],
		"amount": [the amount to be transfered]
	}

The two specified Accounts must exist, and must be different. The specified amount must a be non-negative floating point number. The source Account must have sufficient balance.

Upon success a 201 Created response and a "Location" header pointing to the URL of the newly created Transfer are returned.
