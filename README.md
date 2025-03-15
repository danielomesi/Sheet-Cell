# ShtiCell - A Multi-User Spreadsheet System
As part of a Java Development course, I independently built a spreadsheet management application using JavaFX.

The system enables each cell to store static values, reference other cells, or derive its value from complex function compositions. It supports function composition, allowing cells to compute values based on multiple functions applied across different cells, enabling advanced relationships and calculations.

This application runs with a server in the background using Tomcat. For this project, I utilized several libraries, including JavaFX, OkHttp, Gson, and more.

## Login
Log in to your account here.

![App Screenshot](./readme-images/login.png)

## Dashboard
This page provides an overview of all the sheets in the system and shows the current permissions assigned to different users.

![App Screenshot](./readme-images/dashboard-before-permission-given.png)

## User Permissions
When user doesn't have permission, he can request read/write permissions and wait for the owner to approve/decline.

![App Screenshot](./readme-images/dashboard-on-permission-decision.png)

After that - the change takes effect.

![App Screenshot](./readme-images/dashboard-after-permission-given.png)

## Sheet View
Here, you can view and make changes to the selected sheet (if you have write access; otherwise, you can only view it).

![App Screenshot](./readme-images/sheet-view-write-mode.png)

## Range Functions
A range is a sub-table with functions that can be applied to all its elements. Available functions include average and sum.

![App Screenshot](./readme-images/range-text-box-selected.png)

After selection:

![App Screenshot](./readme-images/range-selected-shown.png)

## Multiple-Function Use (Function Composition)
Cells can be outputs of other cells, which in turn may result from functions, and so on. A mechanism to prevent circular references is implemented.

![App Screenshot](./readme-images/multiple-functions.png)

## Selection a sub-table

To use features like sorting and filtering, the user must select a sub table that will be applied.

![App Screenshot](./readme-images/cell-selection-1.png)

![App Screenshot](./readme-images/cell-selection-2.png)

![App Screenshot](./readme-images/cell-selection-shown.png)

After selection, the corresponding options in the commands menu will be available:

![App Screenshot](./readme-images/commands-after-cell-selection.png)

## Sorting Feature
Ranges (sub-tables) can be sorted by one or more columns (with the option to choose the order of the columns).

![App Screenshot](./readme-images/sort.png)


There's also an option to filter rows based on specific column values (showing only rows with your chosen values).

## Dynamic Analyze
This feature allows you to select a specific cell and define a maximum, minimum, and step. A slider appears, and as you drag it, the value of the cell changes while the table dynamically reflects the updated values.

![App Screenshot](./readme-images/dynamic-analyze-settings.png)

Visualization:

![App Screenshot](./readme-images/gif.gif)


## Versions and Auto Update
Each sheet maintains a version history, which can be accessed from the version chooser.

When multiple users are editing the same sheet, any changes made by one user will trigger a blinking message for the others, notifying them of a new version. To make further changes, users must sync first.

![App Screenshot](./readme-images/new-version-message.png)

## Styling
A side bar offers options for customizing the sheet with animations, different styles, column/row scaling, and changing cell colors and fonts.

![App Screenshot](./readme-images/customize-options.png)

The Sheet View window in Dark Mode.

![App Screenshot](./readme-images/dark-mode.png)

![App Screenshot](./readme-images/dsahboard-dark-mode.png)

