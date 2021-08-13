import * as React from "react";
import { useForm } from "react-hook-form";
import { yupResolver } from '@hookform/resolvers/yup';
import * as yup from 'yup';
import { Zipcode } from "../../../recoil/atoms/Zipcode";
import { StreetAddress } from "../../../recoil/atoms/StreetAddress";
import styled from "styled-components";
import Postcode from "../../molecules/postcode/Postcode";
import { useRecoilValue } from "recoil";
import axios from "axios";
import {useState, useEffect} from 'react';
import { useHistory } from "react-router";


const ErrorP = styled.p`
  color: red;
`;

const StyledForm = styled.form`
  max-width: 800px;
  margin: 0 auto;
`;

const StyeldInput = styled.input`
  display: block;
  box-sizing: border-box;
  width: 100%;
  border-radius: 4px;
  border: 1px solid black;
  padding: 10px 10px;
  margin-bottom: 10px;
  font-size: 14px;
  color: black;
`;

const StyledLabel = styled.label`
  line-height: 2;
  text-align: left;
  display: block;
  margin-bottom: 3px;
  margin-top: 10px;
  font-size: 14px;
  font-weight: 200;
  color: black;
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
  height: number;
  weight: number;
  // role: string;
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
    .max(14, "올바른 전화번호 형식이 아닙니다"),
  gender: yup.number()
    .required()
    .typeError("성별을 선택해주세요"),
  height: yup.number()
    .required('키를 입력해주세요')
    .min(130, '키를 다시 입력해주세요')
    .max(230, '키를 다시 입력해주세요'),
  weight: yup.number()
    .required('몸무게를 입력해주세요')
    .min(35, '체중을 다시 입력해주세요')
    .max(200, '체중을 다시 입력해주세요')
  // role: yup.string()
  //   .required()
  //   .typeError("가입목적을 선택해주세요"),
});

function RegisterStudent() {
  const history = useHistory();
  const [inputPhone, setInputPhone] = useState('');
  const zipcode = useRecoilValue(Zipcode)
  const streetAddress = useRecoilValue(StreetAddress)
  
  const { register, setValue, handleSubmit, formState: {errors} } = useForm<FormValues>({
    resolver: yupResolver(schema)
  });

  const onSubmit = (data: FormValues) => {
    let today = new Date();
    let month = today.getMonth()

    let heights: number[] = []
    for (let i = 0; i < 12; i++) {
      if (i < month+1) {
        heights.push(data.height)
      } else {
        heights.push(-1)
      }
    }
    let weights: number[] = []
    for (let i = 0; i < 12; i++) {
      if (i < month+1) {
        weights.push(data.weight)
      } else {
        weights.push(-1)
      }
    }

    // console.log(data)
    // console.log({
    // "email" : data.email,
    // "password" : data.password,
    // "username" : data.username,
    // "nickname" : data.nickname,
    // "gender" : data.gender,
    // "tel" : data.phone,
    // "zipCode" : data.zipcode,
    // "street" : data.streetAddress,
    // "detailedAddress" : data.detailedAddress,
    // "role" : "ROLE_PTSTUDENT",
    // "monthlyHeights" : heights,
    // "monthlyWeights" : weights
    // })
    axios.post("/api/user", {
    "email" : data.email,
    "password" : data.password,
    "username" : data.username,
    "nickname" : data.nickname,
    "gender" : data.gender,
    "tel" : data.phone,
    "zipCode" : data.zipcode,
    "street" : data.streetAddress,
    "detailedAddress" : data.detailedAddress,
    "role" : "ROLE_PTSTUDENT",
    "monthlyHeights" : heights,
    "monthlyWeights" : weights
    })
    
  
}

  // useEffect(() => {
  //   axios.get('/api/hello')
  //   .then(response => console.log(response))
  // }, [])

  const phoneChange = (e) => {
    const regex = /^[0-9\b -]{0,13}$/;
    if (regex.test(e.target.value)) {
      setInputPhone(e.target.value);
    }
  }

  useEffect(() => {
    if (inputPhone.length === 10) {
      setInputPhone(inputPhone.replace(/(\d{3})(\d{3})(\d{4})/, '$1-$2-$3'));
    }
    if (inputPhone.length === 13) {
      setInputPhone(inputPhone.replace(/-/g, '').replace(/(\d{3})(\d{4})(\d{4})/, '$1-$2-$3'));
    }
  }, [inputPhone]);

  return (
    <StyledForm onSubmit={handleSubmit(onSubmit)} >
      <StyledLabel htmlFor="email">이메일</StyledLabel>
      <StyeldInput type="text" placeholder="이메일"{...register("email")} maxLength={30}/>
      {errors.email?.message && <ErrorP>{errors.email?.message}</ErrorP>}

      <StyledLabel htmlFor="password">비밀번호</StyledLabel>
      <StyeldInput type="password" placeholder="비밀번호(8~20자)" {...register("password")} maxLength={20} />
      {errors.password?.message && <ErrorP>{errors.password?.message}</ErrorP>}

      <StyledLabel htmlFor="confirmPassword">비밀번호 확인</StyledLabel>
      <StyeldInput type="password" placeholder="비밀번호 확인"{...register("confirmPassword")} maxLength={20}/>
      {errors.confirmPassword?.message && <ErrorP>{errors.confirmPassword?.message}</ErrorP>}

      <StyledLabel htmlFor="username">이름</StyledLabel>
      <StyeldInput type="text" placeholder="이름"{...register("username")} maxLength={4}/>
      {errors.username?.message && <ErrorP>{errors.username?.message}</ErrorP>}

      <StyledLabel htmlFor="nickname">닉네임</StyledLabel>
      <StyeldInput type="text" placeholder="닉네임"{...register("nickname")} />
      {errors.nickname?.message && <ErrorP>{errors.nickname?.message}</ErrorP>}

      <StyledLabel htmlFor="zipcode">주소검색</StyledLabel>
      <div style={{display: "flex"}}>
      <Postcode />

      <StyeldInput type="text" placeholder="우편 번호"{...register("zipcode")} value={zipcode} readOnly />
      </div>
      {errors.zipcode?.message && <ErrorP>{errors.zipcode?.message}</ErrorP>}

      <StyeldInput type="text" placeholder="도로명 주소"{...register("streetAddress")} value={streetAddress} readOnly />
      {errors.streetAddress?.message && <ErrorP>{errors.streetAddress?.message}</ErrorP>}

      <StyeldInput type="text" placeholder="상세 주소"{...register("detailedAddress")} />
      {errors.detailedAddress?.message && <ErrorP>{errors.detailedAddress?.message}</ErrorP>}

      <StyledLabel htmlFor="phone">전화번호</StyledLabel>
      <StyeldInput type="text" placeholder="phone"{...register("phone")} maxLength={14}  onChange={phoneChange} value={inputPhone} />
      {errors.phone?.message && <ErrorP>{errors.phone?.message}</ErrorP>}
      
      <StyledLabel htmlFor="gender">성별</StyledLabel>
      <select {...register("gender")} id="gender">
        <option value="">Select</option>
        <option value="0">남성</option>
        <option value="1">여성</option>
      </select>
      {errors.gender?.message && <ErrorP>{errors.gender?.message}</ErrorP>}

      <StyledLabel htmlFor="height">키</StyledLabel>
      <StyeldInput type="number" placeholder="키"{...register("height")} min={130} max={230} />
      {errors.height?.message && <ErrorP>{errors.height?.message}</ErrorP>}

      <StyledLabel htmlFor="weight">체중</StyledLabel>
      <StyeldInput type="number" placeholder="체중"{...register("weight")} min={35} max={200} />
      {errors.weight?.message && <ErrorP>{errors.weight?.message}</ErrorP>}

      {/* <StyledLabel htmlFor="role">가입목적</StyledLabel>
      <select {...register("role")} id="role">
        <option value="">Select</option>
        <option value="ROLE_PTTEACHER">PT트레이너</option>
        <option value="ROLE_PTSTUDENT">PT회원</option>
      </select>
      {errors.role?.message && <ErrorP>{errors.role?.message}</ErrorP>} */}
      
      <StyeldInput 
        type="submit"
        value="회원가입"
        onClick={() => {
          setValue('zipcode', `${zipcode}`);
          setValue('streetAddress', `${streetAddress}`);
      }} 
      />
    </StyledForm>
  );
}

export default RegisterStudent;