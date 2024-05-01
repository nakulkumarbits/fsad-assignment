import React from 'react';
const Paginate = ({ recordsPerPage, totalElements, paginate, previousPage, nextPage }) => {
   const pageNumbers = [];
 
   for (let i = 1; i <= Math.ceil(totalElements / recordsPerPage); i++) {
      pageNumbers.push(i);
   }
//    console.log('pageNumbers : ', pageNumbers);
   
   return (
      <div className="pagination-container">
         <ul className="pagination">
            <li onClick={previousPage} className="page-number mx-1">
            Prev
            </li>
            {/* <button type="button" className="btn btn-info btn-sm mx-1 my-1" onClick={previousPage} >Prev</button> */}
            {pageNumbers.map((number) => (
               <li
                  key={number}
                  onClick={() => paginate(number)}
                  className="page-number mx-1"
               >
                  {number}
               </li>
            ))}
            <li onClick={nextPage} className="page-number mx-1">
            Next
            </li>
            {/* <button type="button" className="btn btn-info btn-sm mx-1 my-1" onClick={nextPage} >Next</button> */}
         </ul>
      </div>
   );
};
 
export default Paginate;