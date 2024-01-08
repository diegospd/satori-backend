# Project Structure
This microservice is structured following the
[hexagonal architecture](https://netflixtechblog.com/ready-for-changes-with-hexagonal-architecture-b315ec967749) pattern.

## Models
The types that are relevant to the microservice. 

## Schemata.out
The types that third parties (.eg database, frontend, other services) require.
There should exist a mapping from models to these types.

## Adapters
Pure functions that convert between internal models and external schemata.

## Logic
Pure functions that implement the business logic.

## Diplomat (Ports)
Impure functions with IO side effects that fetch information for the service
from third parties (database, http calls, filesystem, etc). The return type
of these functions should be internal models, so an adapter should be applied here.

## Controllers
Impure functions for controlling the flow of the application. It may call pure
and other impure functions.
