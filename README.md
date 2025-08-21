# TimveroOS SDK Example Project

A complete example application demonstrating the TimveroOS SDK for building financial applications.

## Overview

This project shows how to use the TimveroOS SDK to implement:
- **Client Management** - Customer data, KYC, document handling
- **Credit Products** - Loan products, applications, approvals
- **Operations Framework** - Business rules, workflows, automated processes  
- **Payment Processing** - Transaction handling, multiple payment gateways

## Prerequisites

- Java 21 or later
- Maven 3.8+
- PostgreSQL 16+

## Quick Start

```bash
git clone https://github.com/TimveroOS/timvero-example.git
cd timvero-example

# Configure database in src/main/resources/application.properties
mvn spring-boot:run

# Access the application
# Admin UI: http://localhost:8081
# Portal API: http://localhost:8082
```

## Documentation

The project includes comprehensive documentation covering:

1. **Platform Overview** - Architecture and core concepts
2. **Getting Started** - First entity creation walkthrough
3. **Data Model** - Database schema and migrations
4. **Forms** - Form handling and validation
5. **Templates** - UI components and customization
6. **Operations** - Business process automation
7. **Payments** - Payment gateway integration

📖 **[View Complete Documentation](src/docs/asciidoc/sdk-guide.adoc)**

## Project Structure

```
src/
├── main/java/com/example/
│   ├── client/          # Client entity and forms
│   ├── application/     # Loan application handling
│   ├── participant/     # Participant management
│   └── config/          # Application configuration
├── main/resources/
│   ├── db/migration/    # Database migrations
│   └── templates/       # Thymeleaf templates
└── docs/asciidoc/       # Documentation source
```

## Key Features Demonstrated

- Entity-Form-Controller pattern
- Automatic CRUD operations
- Form validation and error handling
- Database migrations with Flyway
- Document management integration
- Business rule automation
- Payment gateway integration
- Audit logging and compliance

## Getting Help

- Review the [SDK Guide](src/docs/asciidoc/sdk-guide.adoc) for detailed documentation
- Examine the source code for implementation examples
- Check the test cases for usage patterns
