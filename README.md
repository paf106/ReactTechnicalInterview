# ReactTechnicalInterview

## Description
This application consists in 3 screens, one for each step.
1. Take a photo using the SDK provided
2. Select a photo from the gallery
3. Make a face comparison

It has a simple UI for better understanding.

## Architecture
- MVVM
- DI
- Navigation with safe args
- ViewModel
- Coil for image handling

## Technical decisions
- MVVM architecture for google recommendation
- Use DI for better testability
- Use Navigation with safe args for good practice
- Use one ViewModel to handle the state and lifecycle

## Upcoming features
- Better app icon
- Add tests
- Add a [Stepper library](https://medium.com/@maryamemarzadeh72/stepper-using-jetpack-compose-3765bce0f1b3) to show the user in which step he is 
- Add better progress indicator for matching faces
- Fix import sdk license for better app performance
