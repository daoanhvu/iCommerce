export interface SortModel{
    fieldName: string,
    order: string
}

export class SearchModel{
    page: Number = 0;
    query: any = {};
    size: number = 1;
    sorts: SortModel[] = [];
};