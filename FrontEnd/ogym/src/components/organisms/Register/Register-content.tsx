import * as React from "react";
import { useForm } from "react-hook-form";
import { yupResolver } from '@hookform/resolvers/yup';
import * as yup from 'yup';
import "./registerstyles.css";
import { Zipcode } from "../../../recoil/atoms/Zipcode";
import { StreetAddress } from "../../../recoil/atoms/StreetAddress";
import styled from "styled-components";
import Postcode from "../../molecules/postcode/Postcode";
import { useRecoilValue } from "recoil";


const ErrorP = styled.p`
  color: red;
`;

interface FormValues {
  email: string;
  password: string;
  confirmPassword: string;
  username: string;
  nickname: string;
  zipcode: string;
  streetAddress: string;
  detailedAddress: string;
  phone: string;
  gender: number;
}

const schema = yup.object().shape({
  email: yup.string()
    .required("이메일을 입력해주세요")
    .max(30, "올바른 이메일 형식이 아닙니다")
    .matches(/[0-9a-zA-Z]([-_.]?[0-9a-zA-Z])*@[0-9a-zA-Z]([-_.]?[0-9a-zA-Z])*.[a-zA-Z]{2,3}$/i, "올바른 이메일 형식이 아닙니다"),
  password: yup.string()
    .required("비밀번호를 입력해주세요")
    .min(8, "비밀번호는 8자 이상 입력해주세요")
    .max(20, "비밀번호는 20자 이하 입력해주세요"),
  confirmPassword: yup.string()
    .oneOf([yup.ref("password"), null], "비밀번호가 일치하지 않습니다")
    .required("비밀번호를 입력해주세요"),
  username: yup.string()
    .required("이름을 입력해주세요")
    .matches(/[ㄱ-ㅎㅏ-ㅣ가-힣]/g, "한글을 입력해주세요"),
  nickname: yup.string()
    .required("닉네임을 입력해주세요"),
  zipcode: yup.string()
    .required("주소 검색을 눌러서 입력해주세요"),
  streetAddress: yup.string()
    .required("주소 검색을 눌러서 입력해주세요"),
  detailedAddress: yup.string()
    .required("상세 주소를 입력해주세요"),
  phone: yup.string()
    .required("전화번호를 입력해주세요")
    .max(12, "올바른 전화번호 형식이 아닙니다"),
  gender: yup.number()
    .required()
    .typeError("성별을 선택해주세요"),
    
});

function RegisterContent() {

  const zipcode = useRecoilValue(Zipcode)
  const streetAddress = useRecoilValue(StreetAddress)
  
  const { register, setValue, handleSubmit, formState: {errors} } = useForm<FormValues>({
    resolver: yupResolver(schema)
  });

  const onSubmit = (data: FormValues) => {
    console.log(data)
  }

  return (
    <form onSubmit={handleSubmit(onSubmit)} >
      <label htmlFor="email">이메일</label>
      <input type="text" placeholder="이메일"{...register("email")} maxLength={30}/>
      {errors.email?.message && <ErrorP>{errors.email?.message}</ErrorP>}

      <label htmlFor="password">비밀번호</label>
      <input type="password" placeholder="비밀번호(8~20자)" {...register("password")} maxLength={20} />
      {errors.password?.message && <ErrorP>{errors.password?.message}</ErrorP>}

      <label htmlFor="confirmPassword">비밀번호 확인</label>
      <input type="password" placeholder="비밀번호 확인"{...register("confirmPassword")} maxLength={20}/>
      {errors.confirmPassword?.message && <ErrorP>{errors.confirmPassword?.message}</ErrorP>}

      <label htmlFor="username">이름</label>
      <input type="text" placeholder="이름"{...register("username")} maxLength={4}/>
      {errors.username?.message && <ErrorP>{errors.username?.message}</ErrorP>}

      <label htmlFor="nickname">닉네임</label>
      <input type="text" placeholder="닉네임"{...register("nickname")} />
      {errors.nickname?.message && <ErrorP>{errors.nickname?.message}</ErrorP>}

      <label htmlFor="zipcode">주소검색</label>
      <div style={{display: "flex"}}>
      <Postcode />

      <input type="text" placeholder="우편 번호"{...register("zipcode")} value={zipcode} readOnly />
      </div>
      {errors.zipcode?.message && <ErrorP>{errors.zipcode?.message}</ErrorP>}

      <input type="text" placeholder="도로명 주소"{...register("streetAddress")} value={streetAddress} readOnly />
      {errors.streetAddress?.message && <ErrorP>{errors.streetAddress?.message}</ErrorP>}

      <input type="text" placeholder="상세 주소"{...register("detailedAddress")} />
      {errors.detailedAddress?.message && <ErrorP>{errors.detailedAddress?.message}</ErrorP>}

      <label htmlFor="phone">전화번호</label>
      <input type="text" placeholder="phone"{...register("phone")} maxLength={12}/>
      {errors.phone?.message && <ErrorP>{errors.phone?.message}</ErrorP>}
      
      <label htmlFor="gender">성별</label>
      <select {...register("gender")} id="gender">
        <option value="">Select</option>
        <option value="0">남성</option>
        <option value="1">여성</option>
      </select>
      {errors.gender?.message && <ErrorP>{errors.gender?.message}</ErrorP>}
      
      <input 
        type="submit" 
        onClick={() => {
          setValue('zipcode', `${zipcode}`);
          setValue('streetAddress', `${streetAddress}`);
      }} 
      />
    </form>
  );
}

export default RegisterContent;