# IP-based Resource Shield
This code implements an IP-based access control mechanism for the Admin Control Panel and other admin endpoints.
It verifies the requesting user's IP address against a whitelist predefined IP addresses and grants access only if the IP matches one in the allowed list.

## Why this ?
What if the ```PowerAdmin``` role is granted by mistake to a regular user ? giving him access to critical resources meant exclusively for power users?
To address this risk, the following code introduces an additional layer of security. It ensures that even if the Power Admin role is assigned by mistake, access to sensitive resources is still restricted. 
This is achieved by implementing an ```IP-based access control mechanism```, allowing only verified Power Admins to access resources from specific IP addresses.

## Access Control Logic:
The incoming request's IP address is extracted.
The IP address is compared against a list of allowed IPs stored in a configuration file of database.
If the IP matches an entry in the allowed list, access is granted. Otherwise, access is denied.

## Implementation Details:
### Backend Configuration: 
A file or property (allowed-ips) is maintained with the allowed IP addresses.

### Middleware/Filter: 
The logic is implemented as middleware or an HTTP filter that checks the IP address for every request targeting the Admin Control Panel.

### HTTP Response: 
If the IP is unauthorized, the server returns an HTTP 403 Forbidden response.

## Your Feedback
(^_^) Any feedback is welcome to improve this.