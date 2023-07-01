# API DOCUMENTATION
- This is a loan lending Api modelling the day to day operations in a loan lending company

# Swagger 
- Access swagger ui through http://localhost:8080/swagger-ui/index.html

    # ENDPOINTS
  # Subsciber
  - Create a subscriber http://localhost:8080/api/v1/subscriber/create
  - Update a subsciber details http://localhost:8080/api/v1/subscriber/update/{}
  - Retrieve subscribers http://localhost:8080/api/v1/subscriber/list
  - Get Subscriber by Id http://localhost:8080/api/v1/subscriber/{}
 
  # Loans
  - Request for Loan http://localhost:8080/api/v1/loan/request
  - Configure Loan Period http://localhost:8080/api/v1/loan/configure/period
  - Repayment Api http://localhost:8080/api/v1/loan/configure/repay
