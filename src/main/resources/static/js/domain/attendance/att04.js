const apiUri = "/api/v1/attendance"

$(document).ready(function() {
    initAttendanceRequestGrid();
});

let initAttendanceRequestGrid = () => {
    const gridOptions = {
        rowData: [
            { make: "Tesla", model: "Model Y", price: 64950, electric: true },
            { make: "Ford", model: "F-Series", price: 33850, electric: false },
            { make: "Toyota", model: "Corolla", price: 29600, electric: false },
        ],
        columnDefs: [
            { field: "make", headerName: "제조사" },
            { field: "model", headerName: "모델" },
            { field: "price", headerName: "가격", valueFormatter: p => `${p.value.toLocaleString()} 원` },
            { field: "electric", headerName: "전기차", cellRenderer: p => p.value ? "✅" : "❌" }
        ],
        pagination: true,
        defaultColDef: {
            sortable: true,
            filter: true,
            resizable: true
        }
    };
    const myGridElement = document.querySelector('#myGrid');
    agGrid.createGrid(myGridElement, gridOptions);
};