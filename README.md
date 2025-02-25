ShtiCell - A Multi-User Spreadsheet System
As part of a Java Development course, I independently developed an application for managing spreadsheets. The system allows each cell to hold static values, reference other cells, or result from complex function compositions. It supports function composition, enabling cells to derive values from multiple functions applied across different cells, facilitating intricate relationships and calculations.

Login
Log in to your account here.

![App Screenshot](./readme-images/login.png)

Dashboard
This page provides an overview of all the sheets in the system and shows the current permissions assigned to different users.

![App Screenshot](./readme-images/dashboard.png)


After selecting a sheet, you can opt to view it (if you have the necessary permissions).

![App Screenshot](./readme-images/view-sheet-button.png)


Sheet View
Here, you can view and make changes to the selected sheet (if you have write access; otherwise, you can only view it).

![App Screenshot](./readme-images/sheet-view.png)

Range Functions
A range is a sub-table with functions that can be applied to all its elements. Available functions include average and sum.

![App Screenshot](./readme-images/sum-example.png)

Multiple-Function Use (Function Composition)
Cells can be outputs of other cells, which in turn may result from functions, and so on. A mechanism to prevent circular references is implemented.

![App Screenshot](./readme-images/multiple-functions.png)

Sorting Feature
Ranges (sub-tables) can be sorted by one or more columns (with the option to choose the order of the columns).

![App Screenshot](./readme-images/sort.png)


There's also an option to filter rows based on specific column values (showing only rows with your chosen values).
Dynamic Analyze
This feature allows you to select a specific cell and define a maximum, minimum, and step. A slider appears, and as you drag it, the value of the cell changes while the table dynamically reflects the updated values.

![App Screenshot](./readme-images/dynamic-analyze.png)


User Permissions
Users can view sheets and request read and/or write permissions.

![App Screenshot](./readme-images/request-permission.png)

Only the owner can approve or deny these requests.

![App Screenshot](./readme-images/grant-permission.png)

Versions and Auto Update
Each sheet maintains a version history, which can be accessed from the version chooser.

When multiple users are editing the same sheet, any changes made by one user will trigger a blinking message for the others, notifying them of a new version. To make further changes, users must sync first.

![App Screenshot](./readme-images/new-version-message.png)

Styling
A side bar offers options for customizing the sheet with animations, different styles, column/row scaling, and changing cell colors and fonts.

![App Screenshot](./readme-images/customize-options.png)

The Sheet View window in Dark Mode.

![App Screenshot](./readme-images/dark-mode.png)

