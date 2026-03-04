# Shop Simulator - Inventory Management System

A Java-based console application for managing shop inventory with JSON persistence, low stock alerts, and comprehensive validation.

## Overview

Shop Simulator is a robust inventory management system designed for small to medium-sized retail businesses. It provides a command-line interface for managing products, tracking stock levels, and maintaining inventory data with automatic JSON persistence.

## Features

### Core Functionality
- **Product Management**: Register, update, and track products with unique codes
- **Stock Control**: Increase or decrease product quantities with validation
- **Inventory Validation**: Check stock availability for required quantities
- **Low Stock Alerts**: Automatic alerts when products fall below threshold (5 units)
- **Data Persistence**: JSON-based storage for products and alerts
- **Auto-save Mode**: Optional automatic saving after each operation

### Data Management
- **JSON Persistence**: All data stored in human-readable JSON format
- **Manual Save/Load**: Explicit control over data persistence
- **Data Recovery**: Reload functionality to restore from saved files
- **Alert Management**: View and clear low stock notifications

## Architecture

The application follows a clean, layered architecture:

```
┌─────────────────┐
│     Main.java   │ ← User Interface Layer
├─────────────────┤
│  SysInventory   │ ← Business Logic Layer
├─────────────────┤
│ Product/Validator│ ← Domain Models
├─────────────────┤
│   JsonManager   │ ← Data Persistence Layer
└─────────────────┘
```

### Package Structure

- **`org.Main`**: Main application entry point and user interface
- **`org.app.SysInventory`**: Core business logic and inventory management
- **`org.logic.Product`**: Product entity model
- **`org.logic.Validator`**: Input validation and business rules
- **`org.persistence.JsonManager`**: JSON file operations and data persistence

## Installation & Setup

### Prerequisites
- Java 17 or higher
- Gradle 7.0 or higher

### Build Instructions

1. Clone the repository:
```bash
git clone <repository-url>
cd ShopSimulator
```

2. Build the project:
```bash
./gradlew build
```

3. Run the application:
```bash
./gradlew run
```

### Dependencies

- **Jackson JSON Processor**: For JSON serialization/deserialization
- **JUnit 5**: For unit testing
- **Gradle**: Build automation and dependency management

## Usage Guide

### Main Menu Options

1. **Register Product**: Add new products to inventory
   - Code: Unique alphanumeric identifier
   - Name: Product name (min 3 characters)
   - Price: Positive decimal value
   - Initial Quantity: Non-negative integer

2. **Augment Stock**: Increase product quantity
   - Enter product code and quantity to add

3. **Reduce Stock**: Decrease product quantity
   - Enter product code and quantity to remove
   - Validates sufficient stock before reduction

4. **Validate Inventory**: Check stock availability
   - Verify if required quantity is available

5. **Show All Products**: Display complete product list
   - Shows low stock warnings where applicable

6. **Show Alerts**: View active low stock notifications

7. **Show Low Stock Products**: Filter products below threshold

8. **Clear Alerts**: Remove all active notifications

9. **Save Data Manually**: Explicit data persistence

10. **Reload Data**: Restore from JSON files

11. **Toggle Auto-save**: Enable/disable automatic saving

0. **Exit**: Save data and exit application

### Data Storage

All data is stored in the `data/` directory:
- `products.json`: Product inventory data
- `alerts.json`: Low stock alert notifications

## Validation Rules

### Product Validation
- **Code**: Alphanumeric characters only, required
- **Name**: Minimum 3 characters, required
- **Price**: Must be greater than 0
- **Quantity**: Must be non-negative

### Business Rules
- Product codes must be unique
- Stock reduction requires sufficient inventory
- Low stock threshold: 5 units
- Operation quantities must be positive

## Error Handling

The application provides comprehensive error handling:
- Input validation with descriptive error messages
- Graceful handling of missing data files
- Recovery from corrupted JSON files
- Detailed logging for debugging

## Development

### Running Tests
```bash
./gradlew test
```

### Code Quality
The project includes SonarQube integration for code quality analysis:
```bash
./gradlew sonarqube
```

### Project Structure
```
ShopSimulator/
├── src/
│   ├── main/
│   │   └── java/
│   │       └── org/
│   │           ├── Main.java
│   │           ├── app/
│   │           │   └── SysInventory.java
│   │           ├── logic/
│   │           │   ├── Product.java
│   │           │   └── Validator.java
│   │           └── persistence/
│   │               └── JsonManager.java
│   └── test/
├── data/
│   ├── products.json
│   └── alerts.json
├── build.gradle
├── settings.gradle
└── README.md
```

## Configuration

### Low Stock Threshold
Modify `MINIMUM_STOCK_ALERT` in `Validator.java` to change the alert threshold:
```java
public static final int MINIMUM_STOCK_ALERT = 5; // Default: 5 units
```

### File Paths
Data storage paths are configured in `JsonManager.java`:
```java
private static final String DATA_DIRECTORY = "data";
private static final String PRODUCTS_FILE = DATA_DIRECTORY + File.separator + "products.json";
private static final String ALERTS_FILE = DATA_DIRECTORY + File.separator + "alerts.json";
```

## Contributing

1. Follow Java naming conventions
2. Add appropriate validation for new features
3. Include comprehensive error handling
4. Write unit tests for new functionality
5. Update documentation for API changes

## License

This project is licensed under the MIT License - see the LICENSE file for details.

## Version History

- **v1.0-SNAPSHOT**: Initial release with core inventory management features
  - Product registration and management
  - Stock control operations
  - JSON persistence
  - Low stock alerts
  - Comprehensive validation

## Support

For issues, questions, or contributions, please refer to the project's issue tracker or contact the development team.