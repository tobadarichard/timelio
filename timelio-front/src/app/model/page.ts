export interface Page<T>{
    content: Array<T>,
    last: boolean,
    totalPages: number,
    totalElements: number,
    first: boolean,
    number: number,
    numberOfElements: number;
    empty: boolean
}