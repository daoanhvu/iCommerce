export interface ServiceResponse<T>{
    httpStatus: Number,
    serviceCode: number,
    serviceMessage: string,
    result: T
};

export interface PagedResult<T>{
    content: T[],
    numberOfElements: number,
    page: number,
    size: number,
    totalElements: number,
    totalPages: number
};

export interface ErrorResponse {
    serviceCode: number,
    serviceMessage: string
}