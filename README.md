# SpaceShipAPI

API endpoints definition:
- GET** /api/spacecrafts?page={number1}&size={number1} :returns the page {number1} with {number2} elements in a list format. If there's not enough elements, returns an empty list.
- GET** /api/spacecrafts/{number} : returns a list with the element with {number} id. If it doesn't exist, returns an empty list.
- GET** /api/spacecrafts?name={name} : returns a list with all the elements in the database which names include the given substring.
- POST** /api/spacecrafts :creates a spacecraft element in the database, with the given Json values*. Returns a bad request status if there's already an element with the given id or a valid id wasn't provided.
- PUT** /api/spacecrafts :updates a current spacecraft element searching for the id, with the remaining Json values*, If the id isn't found, returns a not found code
- DELETE /api/spacecrafts/{number} :searches the database for the element with the given id, deleting it. Otherwise, returns a not found code.


*API request JSON format<br />
ShipDTO: {
    Integer id;
    String name;
    String firstAppearance;
    String dateFirstAppearance;
}


**API returned JSON format:<br />
ShipResponseDTO{
    List<ShipDTO> ships;
    String responseText;
}

